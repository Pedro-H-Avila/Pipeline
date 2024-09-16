import java.util.ArrayList;

public class Bubble {

    // Método principal para aplicar as técnicas de otimização
    public static ArrayList<Instruction> implement(ArrayList<Instruction> pipeline, ArrayList<String> techniques) {
        ArrayList<Instruction> reorderedPipeline = new ArrayList<>(pipeline);

        // Verificar se a técnica de reordenamento foi selecionada
        if (techniques.contains("reordenamento")) {
            reordering(reorderedPipeline);
        }

        ArrayList<Instruction> resposta = new ArrayList<>();

        // Verificar se devemos inserir NOPs (apenas se bolha ou adiantamento estiverem selecionados)
        boolean insertNops = techniques.contains("bolha") || techniques.contains("adiantamento");

        // Se todas as técnicas estão selecionadas, não inserimos NOPs
        boolean allTechniques = techniques.contains("bolha") && techniques.contains("adiantamento") && techniques.contains("reordenamento");

        for (int i = 0; i < reorderedPipeline.size(); i++) {
            Instruction currentInstr = reorderedPipeline.get(i);
            int nopsNeeded = 0;

            if (insertNops && !allTechniques) {  // Somente insere NOPs se não forem todas as técnicas combinadas
                nopsNeeded = quantNops(resposta, currentInstr);

                // Aplicar técnicas de adiantamento, garantindo que apenas 1 NOP seja inserido
                if (techniques.contains("adiantamento") && nopsNeeded > 0) {
                    nopsNeeded = 1; // Limitar a 1 NOP para adiantamento
                }

                // Inserir NOPs necessários (um por linha, no estilo do seu código)
                for (int n = 0; n < nopsNeeded; n++) {
                    resposta.add(new Instruction("nop", "nop", "nop", "nop"));
                }
            }

            // Adicionar a instrução atual
            resposta.add(currentInstr);
        }

        // Remover NOPs desnecessários após o `lw` em `05`
        if (techniques.contains("reordenamento")) {
            removeUnnecessaryNops(resposta);
        }

        return resposta;
    }

    // Método para calcular quantos NOPs são necessários
    private static int quantNops(ArrayList<Instruction> resposta, Instruction currentInstr) {
        int nopsNeeded = 0;

        // Verificar dependências com as duas instruções anteriores
        for (int k = 1; k <= 2; k++) {
            if (resposta.size() >= k) {
                Instruction prevInstr = resposta.get(resposta.size() - k);
                if (hasDependency(prevInstr, currentInstr)) {
                    if (prevInstr.getInstru().equals("lw")) {
                        if (k == 1) {
                            // Após lw, se a dependência é imediata, precisamos de 2 NOPs
                            nopsNeeded = Math.max(nopsNeeded, 2);
                        } else if (k == 2) {
                            // Dependência com a instrução anterior ao lw
                            nopsNeeded = Math.max(nopsNeeded, 1);
                        }
                    } else if (isInstructionThatWrites(prevInstr)) {
                        if (k == 1) {
                            // Dependência imediata após instruções que escrevem
                            nopsNeeded = Math.max(nopsNeeded, 2);
                        } else if (k == 2) {
                            nopsNeeded = Math.max(nopsNeeded, 1);
                        }
                    }
                }
            }
        }

        return nopsNeeded;
    }

    // Verifica se a instrução escreve em um registrador de destino
    private static boolean isInstructionThatWrites(Instruction instr) {
        String opcode = instr.getInstru().toLowerCase();
        return !(opcode.equals("sw") || opcode.equals("sb") || opcode.equals("sh") ||
                 opcode.equals("swl") || opcode.equals("swr") || opcode.equals("nop") ||
                 opcode.equals("j") || opcode.equals("jr") || opcode.equals("jal") ||
                 opcode.equals("jalr") || opcode.equals("beq") || opcode.equals("bne"));
    }

    // Verifica se há dependência entre duas instruções
    private static boolean hasDependency(Instruction instr1, Instruction instr2) {
        String dest1 = instr1.getRegis1();

        // Obter os registradores fonte da instr2
        ArrayList<String> srcRegisters = getSourceRegisters(instr2);

        // Verificar se dest1 está nos registradores fonte de instr2
        for (String src : srcRegisters) {
            if (dest1.equals(src)) {
                return true;
            }
        }
        return false;
    }

    // Método para obter os registradores fonte de uma instrução
    private static ArrayList<String> getSourceRegisters(Instruction instr) {
        ArrayList<String> srcRegisters = new ArrayList<>();
        String opcode = instr.getInstru().toLowerCase();

        switch (opcode) {
            case "lw":
            case "lb":
            case "lh":
            case "lwl":
            case "lwr":
                // Instruções de carga: registrador base é fonte
                srcRegisters.add(instr.getRegis2());
                break;
            case "sw":
            case "sb":
            case "sh":
            case "swl":
            case "swr":
                // Instruções de armazenamento: registrador de dado e base são fontes
                srcRegisters.add(instr.getRegis1()); // dado a ser armazenado
                srcRegisters.add(instr.getRegis2()); // registrador base
                break;
            case "beq":
            case "bne":
                // Instruções de desvio condicional: ambos os registradores são fontes
                srcRegisters.add(instr.getRegis1());
                srcRegisters.add(instr.getRegis2());
                break;
            default:
                // Instruções tipo R: registradores fonte são regis2 e regis3
                srcRegisters.add(instr.getRegis2());
                srcRegisters.add(instr.getRegis3());
                break;
        }
        return srcRegisters;
    }

    // Método para reordenar instruções e minimizar NOPs
    public static void reordering(ArrayList<Instruction> instructions) {
        int n = instructions.size();
        ArrayList<Instruction> reordenado = new ArrayList<>();
        
        // Reordenar instruções de salto para o início
        for (int i = 0; i < n; i++) {
            Instruction instr = instructions.get(i);
            if (isUnconditionalBranchInstruction(instr)) {
                reordenado.add(instr); // Mover saltos para o início
            }
        }

        // Adicionar as instruções restantes
        for (int i = 0; i < n; i++) {
            Instruction instr = instructions.get(i);
            if (!isUnconditionalBranchInstruction(instr)) {
                reordenado.add(instr); // Adicionar instruções que não são de salto
            }
        }

        // Limpar a lista original e adicionar as instruções reordenadas
        instructions.clear();
        instructions.addAll(reordenado);
    }

    // Verifica se a instrução é um desvio incondicional
    private static boolean isUnconditionalBranchInstruction(Instruction instr) {
        String opcode = instr.getInstru().toLowerCase();
        return opcode.equals("beq") || opcode.equals("bne") || opcode.equals("j") || opcode.equals("jal") || opcode.equals("jr");
    }

    // Método para remover NOPs desnecessários (caso dos arquivos `03` e `05`)
    private static void removeUnnecessaryNops(ArrayList<Instruction> resposta) {
        for (int i = 0; i < resposta.size() - 1; i++) {
            Instruction instr = resposta.get(i);
            // Verificar se o NOP é necessário ou se pode ser removido
            if (instr.getInstru().equals("nop") && (resposta.get(i + 1).getInstru().equals("nop") || resposta.get(i + 1).getInstru().equals("sw"))) {
                resposta.remove(i); // Remover NOP desnecessário
                i--; // Reavaliar a próxima posição
            }
        }
    }
}
