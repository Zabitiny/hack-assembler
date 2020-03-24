public class Code {

    public byte comp(String mnemonic) {
        switch (mnemonic) {
            case ("0"): return 42;
            case ("1"): return 63;
            case ("-1"): return 58;
            case ("D"): return 12;
            case ("A"): return 48;
            case ("M"): return 112;
            case ("!D"): return 13;
            case ("!A"): return 49;
            case ("!M"): return 113;
            case ("-D"): return 15;
            case ("-A"): return 51;
            case ("-M"): return 115;
            case ("D+1"): return 31;
            case ("A+1"): return 55;
            case ("M+1"): return 119;
            case ("D-1"): return 14;
            case ("A-1"): return 50;
            case ("M-1"): return 114;
            case ("D+A"): return 2;
            case ("D+M"): return 66;
            case ("D-A"): return 19;
            case ("D-M"): return 83;
            case ("A-D"): return 7;
            case ("M-D"): return 71;
            case ("D&A"): return 0;
            case ("D&M"): return 64;
            case ("D|A"): return 21;
            default: return 85;
        }
    }

    public byte dest(String mnemonic) {
        if(mnemonic == null) {
            return 0;
        }

        byte rtn = 0;
        int n = mnemonic.length();
        for(int i=0; i < n; i++) {
            switch (mnemonic.charAt(i)) {
                case ('A'): rtn += 4;
                            break;
                case ('D'): rtn += 2;
                            break;
                case ('M'): rtn += 1;
            }
        }
        return rtn;
    }

    public byte jump(String mnemonic) {
        if(mnemonic == null) {
            return 0;
        }
        
        byte rtn = 0;
        int n = mnemonic.length();
        for(int i=1; i < n; i++) {
            if(mnemonic.charAt(i) == 'M') {
                return 7;
            }
            else if(mnemonic.charAt(i) == 'N') {
                return 5;
            }
            else if(mnemonic.charAt(i) == 'G') {
                rtn += 1;
            }
            else if(mnemonic.charAt(i) == 'E') {
                rtn += 2;
            }
            else if(mnemonic.charAt(i) == 'L') {
                rtn += 4;
            }
        }
        return rtn;
    }
}