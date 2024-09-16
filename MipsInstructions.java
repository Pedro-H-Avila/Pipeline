import java.util.ArrayList;
import java.util.List;

public class MipsInstructions {

    // Listas para armazenar as instruções de destino e de origem
    private List<String> destinationInstructions;
    private List<String> sourceInstructions;
    private List<String> advanceMem;

    public MipsInstructions() {
        destinationInstructions = new ArrayList<>();
        sourceInstructions = new ArrayList<>();
        advanceMem = new ArrayList<>();

        destinationInstructions.add("lb");
        destinationInstructions.add("lh");
        destinationInstructions.add("lwl");
        destinationInstructions.add("lw");
        destinationInstructions.add("lbu");
        destinationInstructions.add("lhu");
        destinationInstructions.add("lwr");
        destinationInstructions.add("add");
        destinationInstructions.add("addu");
        destinationInstructions.add("sub");
        destinationInstructions.add("subu");
        destinationInstructions.add("and");
        destinationInstructions.add("or");
        destinationInstructions.add("xor");
        destinationInstructions.add("nor");
        destinationInstructions.add("slt");
        destinationInstructions.add("sltu");
        destinationInstructions.add("addi");
        destinationInstructions.add("addiu");
        destinationInstructions.add("slti");
        destinationInstructions.add("sltiu");
        destinationInstructions.add("andi");
        destinationInstructions.add("ori");
        destinationInstructions.add("xori");
        destinationInstructions.add("lui");
        destinationInstructions.add("sll");
        destinationInstructions.add("srl");
        destinationInstructions.add("sra");
        destinationInstructions.add("sllv");
        destinationInstructions.add("srlv");
        destinationInstructions.add("srav");
        destinationInstructions.add("mfhi");
        destinationInstructions.add("mflo");
        destinationInstructions.add("jalr");

        sourceInstructions.add("sw");
        sourceInstructions.add("sb");
        sourceInstructions.add("sh");
        sourceInstructions.add("swl");
        sourceInstructions.add("swr");
        sourceInstructions.add("mthi");
        sourceInstructions.add("mtlo");
        sourceInstructions.add("mult");
        sourceInstructions.add("multu");
        sourceInstructions.add("div");
        sourceInstructions.add("divu");
        sourceInstructions.add("jr");
        sourceInstructions.add("bltz");
        sourceInstructions.add("bgez");
        sourceInstructions.add("bltzal");
        sourceInstructions.add("bgezal");
        sourceInstructions.add("j");
        sourceInstructions.add("jal");
        

        advanceMem.add("lb");
        advanceMem.add("lh");
        advanceMem.add("lwl");
        advanceMem.add("lw");
        advanceMem.add("lbu");
        advanceMem.add("lhu");
        advanceMem.add("lwr");
        advanceMem.add("sb");
        advanceMem.add("sh");
        advanceMem.add("swl");
        advanceMem.add("sw");
        advanceMem.add("swr");
        advanceMem.add("j");
        advanceMem.add("jal");
        advanceMem.add("jr");
        advanceMem.add("jalr");
        advanceMem.add("sll");
        advanceMem.add("srl");
        advanceMem.add("sra");
        advanceMem.add("sllv");
        advanceMem.add("srlv");
        advanceMem.add("srav");
        
    }

    public boolean isDest(String instru) {
        if (destinationInstructions.contains(instru)) {
            return true;
        }
        return false;

    }

    public boolean isOri(String instru) {
        if (sourceInstructions.contains(instru)) {
            return true;
        }
        return false;

    }

    public boolean isMem(String instru) {
        if (advanceMem.contains(instru)) {
            return true;
        }
        return false;
    }
}
