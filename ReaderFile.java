import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReaderFile {

    // Método principal para processar os arquivos
    public static void processarArquivos() {
        // Mapear arquivos aos conjuntos de técnicas
        String[] arquivosGrupo1 = {"TESTE-01.txt", "TESTE-02.txt"};
        String[] arquivosGrupo2 = {"TESTE-03.txt", "TESTE-04.txt"};
        String[] arquivosGrupo3 = {"TESTE-05.txt", "TESTE-06.txt", "TESTE-07.txt"};
        String[] arquivosGrupo4 = {"TESTE-08.txt", "TESTE-09.txt", "TESTE-10.txt"};

        // Processar cada grupo com as técnicas correspondentes
        processarGrupoDeArquivos(arquivosGrupo1, new String[]{"bolha"});
        processarGrupoDeArquivos(arquivosGrupo2, new String[]{"bolha", "adiantamento"});
        processarGrupoDeArquivos(arquivosGrupo3, new String[]{"reordenamento"});
        processarGrupoDeArquivos(arquivosGrupo4, new String[]{"bolha", "adiantamento", "reordenamento"});
    }

    // Método para processar um grupo de arquivos com as técnicas especificadas
    private static void processarGrupoDeArquivos(String[] arquivos, String[] techniquesArray) {
        ArrayList<String> techniques = new ArrayList<>();
        for (String tech : techniquesArray) {
            techniques.add(tech);
        }

        for (String caminhoDoArquivo : arquivos) {
            processarArquivo(caminhoDoArquivo, techniques);
        }
    }

    // Método para processar um único arquivo
    public static void processarArquivo(String caminhoDoArquivo, ArrayList<String> techniques) {
        ArrayList<Instruction> pipeline = new ArrayList<>();
        MipsInstructions mips = new MipsInstructions();
        String arquivoSaida = caminhoDoArquivo.replace(".txt", "-RESULTADO.txt");

        try {
            File arq = new File(caminhoDoArquivo);

            if (!arq.exists()) {
                System.out.println("O arquivo " + caminhoDoArquivo + " não existe!");
                return;
            }

            Scanner scanner = new Scanner(arq);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignorar linhas vazias
                if (line.isEmpty()) {
                    continue;
                }

                // Tratamento especial para instruções NOP
                if (line.equalsIgnoreCase("nop")) {
                    pipeline.add(new Instruction("nop", "nop", "nop", "nop"));
                    continue;
                }

                // Tokenizar a linha considerando espaços, vírgulas, parênteses e tabulações
                StringTokenizer regis = new StringTokenizer(line, ",$() \t");

                ArrayList<String> tokens = new ArrayList<>();
                while (regis.hasMoreTokens()) {
                    tokens.add(regis.nextToken().toLowerCase());
                }

                String opcode = tokens.get(0);
                Instruction instruction;

                if (opcode.equals("lw") || opcode.equals("lb") || opcode.equals("lh") ||
                    opcode.equals("lwl") || opcode.equals("lwr")) {
                    // Formato: lw $destino, deslocamento($base)
                    instruction = new Instruction(opcode, tokens.get(1), tokens.get(3), tokens.get(2));
                } else if (opcode.equals("sw") || opcode.equals("sb") || opcode.equals("sh") ||
                           opcode.equals("swl") || opcode.equals("swr")) {
                    // Formato: sw $fonte, deslocamento($base)
                    instruction = new Instruction(opcode, tokens.get(1), tokens.get(3), tokens.get(2));
                } else if (opcode.equals("beq") || opcode.equals("bne")) {
                    // Formato: beq $reg1, $reg2, label
                    instruction = new Instruction(opcode, tokens.get(1), tokens.get(2), tokens.get(3));
                } else if (mips.isDest(opcode)) {
                    // Instruções tipo R
                    instruction = new Instruction(opcode, tokens.get(1), tokens.get(2), tokens.get(3));
                } else {
                    // Outros casos
                    instruction = new Instruction(opcode, tokens.get(1), tokens.get(2), tokens.get(3));
                }

                pipeline.add(instruction);
            }

            // Gerar a saída no arquivo correspondente
            gerarArquivoSaida(arquivoSaida, pipeline, techniques);

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    // Método para gerar o arquivo de saída
    public static void gerarArquivoSaida(String arquivoSaida, ArrayList<Instruction> pipeline, ArrayList<String> techniques) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoSaida))) {
            // Chamando Bubble para implementar a lógica de otimização
            ArrayList<Instruction> resultado = Bubble.implement(pipeline, techniques);

            for (Instruction instru : resultado) {
                writer.write(instru.getAllValues() + "\n");
            }
            System.out.println("Arquivo gerado com sucesso: " + arquivoSaida);
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo: " + e.getMessage());
        }
    }
}
