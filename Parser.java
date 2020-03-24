import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser extends Code {

    private Scanner scan;
    private String currComm;

    public enum Command {
        A_COMMAND,
        C_COMMAND,
        L_COMMAND;
    }

    public Parser(File file) throws FileNotFoundException {
        scan = new Scanner(file);
    }

    public boolean hasMoreCommands() {
        return scan.hasNextLine();
    }

    public void advance() {
        currComm = scan.nextLine();
        int n = currComm.length();
        while(n == 0){
            currComm = scan.nextLine();
            n = currComm.length();
        }
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < n; i++) {
            char ch = currComm.charAt(i);
            if(ch == ' ') {
                continue;
            }
            else if(ch == '/' && currComm.charAt(i+1) == '/') {
                if(sb.length() == 0){
                    currComm = scan.nextLine();
                    n = currComm.length();
                    i=-1;
                    continue;
                } else {
                    currComm = sb.toString();
                    break;
                }
            }
            sb.append(ch);
        }

        currComm = sb.toString();
        if(currComm.length() == 0) advance();
    }

    public Command commandType() {
        int n = currComm.length();
        
        for(int i=0; i < n; i++){
            char ch = currComm.charAt(i);
            if(ch == '@') return Command.A_COMMAND;
            else if(ch == '=' || ch == ';') return Command.C_COMMAND;
            else if(ch == '(' || ch == ')') return Command.L_COMMAND;
        }
        return null;
    }

    /**
     * grabs symbol from current instruction current instruction must be an
     * A_COMMAND or L_COMMAND
     * 
     * @throws Exception symbols cannot start with a digit
     */
    public String symbol() throws Exception {
        if(currComm == null) {
            if(hasMoreCommands()) advance();
            else return null;
        }
        if(commandType() == Command.C_COMMAND) return null;

        StringBuilder rtn = new StringBuilder();
        int n = currComm.length();
        boolean isDigit = false;
        for(int i=0; i < n; i++) {    
            char ch = currComm.charAt(i);
            if(isDigit) {
                if(!Character.isDigit(ch)) {
                    throw new Exception("InvalidSymbolException: "+ currComm);
                }
                rtn.append(ch);
            }
            else {
                if(Character.isDigit(ch = currComm.charAt(i)) 
                    && rtn.length() == 0) {
                    rtn.append(ch);
                    isDigit = true;
                }
                else if(Character.isLetterOrDigit(ch)
                        || ch == '_' || ch == '.' 
                        || ch == '$' || ch == ':') { rtn.append(ch); }
            }
        }
        return rtn.toString();
    }

    public String comp() {
        StringBuilder rtn = new StringBuilder();
        int n = currComm.length();
        int i;
        for(i=0; i < n; i++) {
            char ch = currComm.charAt(i);
            if(ch == ';') {
                return rtn.toString();
            }
            else if(ch == '=') {
                rtn.delete(0, rtn.length());
            }
            else if(ch == ' ') {
                continue;
            }
            else {
                rtn.append(ch);
            }
        }

        return rtn.toString();
    }

    public String dest() {
        StringBuilder rtn = new StringBuilder();
        int n = currComm.length();
        for(int i=0; i < n; i++) {
            char ch = currComm.charAt(i);
            if(ch == '='){
                return rtn.toString();
            }
            else if(ch == ' ') {
                continue;
            }
            rtn.append(ch);
        }

        return null;
    }

    public String jump() {
        StringBuilder rtn = new StringBuilder();
        int n = currComm.length();
        int i;
        for(i=0; i < n; i++) {
            char ch = currComm.charAt(i);
            if(ch == 'J') {
                rtn.append(ch);
                i++;
                break;
            }
            else if(i == n-1) return null;
        }
        for(; i < n; i++) {
            char ch = currComm.charAt(i);
            rtn.append(ch);
        }
        return rtn.toString();
    }
}