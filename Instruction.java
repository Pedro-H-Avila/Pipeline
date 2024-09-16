public class Instruction {
     private String instru;   // Opcode da instrução
     private String regis1;   // Primeiro operando
     private String regis2;   // Segundo operando
     private String regis3;   // Terceiro operando
 
     // Construtor
     public Instruction(String instru, String regis1, String regis2, String regis3) {
         this.instru = instru;
         this.regis1 = regis1;
         this.regis2 = regis2;
         this.regis3 = regis3;
     }
 
     // Getters e Setters
     public String getInstru() {
         return instru;
     }
 
     public void setInstru(String instru) {
         this.instru = instru;
     }
 
     public String getRegis1() {
         return regis1;
     }
 
     public void setRegis1(String regis1) {
         this.regis1 = regis1;
     }
 
     public String getRegis2() {
         return regis2;
     }
 
     public void setRegis2(String regis2) {
         this.regis2 = regis2;
     }
 
     public String getRegis3() {
         return regis3;
     }
 
     public void setRegis3(String regis3) {
         this.regis3 = regis3;
     }
 
     // Método para formatar a instrução para saída
     public String getAllValues() {
          if (instru.equalsIgnoreCase("nop")) {
              return "NOP";
          }
      
          switch (instru.toLowerCase()) {
              // Instruções de carregamento
              case "lw":
              case "lb":
              case "lh":
              case "lwl":
              case "lwr":
                  // Formato: lw $destino, deslocamento($base)
                  return instru + " $" + regis1 + ", " + regis3 + "($" + regis2 + ")";
              // Instruções de armazenamento
              case "sw":
              case "sb":
              case "sh":
              case "swl":
              case "swr":
                  // Formato: sw $fonte, deslocamento($base)
                  return instru + " $" + regis1 + ", " + regis3 + "($" + regis2 + ")";
              // Instruções de desvio condicional
              case "beq":
              case "bne":
                  // Formato: beq $reg1, $reg2, label
                  return instru + " $" + regis1 + ", $" + regis2 + ", " + regis3;
              // Instruções tipo R
              default:
                  // Formato: add $destino, $fonte1, $fonte2
                  return instru + " $" + regis1 + ", $" + regis2 + ", $" + regis3;
          }
      }
      
 }
 