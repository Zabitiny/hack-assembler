import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class Assembler extends Parser {

    private static int i;

    public Assembler(File file) throws FileNotFoundException {
        super(file);
    }

    private static String A_CommToBin(int num) {
        StringBuilder rtn = new StringBuilder("000000000000000");
        int i = rtn.length() - 1;
        while(num > 0) {
            if(num % 2 == 1) rtn.setCharAt(i, '1');
            num /= 2;
            i--;
        }
        return rtn.toString();
    }

    private static String C_CommToBin(byte comp, byte dest, byte jump) {
        StringBuilder rtn = new StringBuilder();
        
        for(int i=6; i >= 0; i--) {
            rtn.append((comp >> i)&1);
        }

        for(int i=2; i >= 0; i--) {
            rtn.append((dest >> i)&1);
        }

        for(int i=2; i >= 0; i--) {
            rtn.append((jump >> i)&1);
        }
        return rtn.toString();
    }

    private static Hashtable<String, Integer> buildTable(File file, Assembler ass) throws Exception {
        Hashtable<String, Integer> rtn = new Hashtable<>();
        rtn.put("SP", 0);
        rtn.put("LCL", 1);
        rtn.put("ARG", 2);
        rtn.put("THIS", 3);
        rtn.put("THAT", 4);
        for(i=0; i < 16; i++) rtn.put("R"+i, i);
        rtn.put("SCREEN", 16384);
        rtn.put("KBD", 24576);
        int instrCounter = 0;
        while(ass.hasMoreCommands()) {
            ass.advance();
            String sym = ass.symbol();
            if(ass.commandType() == Command.L_COMMAND) {
                if(rtn.containsKey(sym)) System.out.println(sym + rtn.remove(sym));
                rtn.put(sym, instrCounter);
            }
            else instrCounter++;
        }

        return rtn;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("../pong/Pong.asm");
        
        Assembler assembler = new Assembler(file);
        Hashtable<String, Integer> symbolTable = buildTable(file, assembler);

        final String FILENAME = "Pong";
        try {
            Assembler ass = new Assembler(file);
            FileWriter fw = new FileWriter(new File(FILENAME + ".hack"));

            while(ass.hasMoreCommands()) {
                ass.advance();
                if(ass.commandType() == Command.A_COMMAND) {
                    String sym = ass.symbol();
                    //constant
                    if(sym.matches("-?\\d+")) 
                        fw.write("0" + A_CommToBin(Integer.parseInt(sym)) 
                            + System.lineSeparator());
                    //symbol
                    else {
                        if(!symbolTable.containsKey(sym)){
                            symbolTable.put(sym, i);
                            i++;
                        }
                        fw.write("0"+ A_CommToBin(symbolTable.get(sym)) 
                            + System.lineSeparator());
                    }
                }
                else if(ass.commandType() == Command.C_COMMAND) {
                    fw.write("111" + C_CommToBin(ass.comp(ass.comp()), 
                            ass.dest(ass.dest()), ass.jump(ass.jump()))
                            + System.lineSeparator());
                }
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}