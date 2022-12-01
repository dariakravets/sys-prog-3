import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Lexer {

    private Pattern identifier = Pattern.compile("^\\$[a-zA-Z0-9]");
    private Pattern decNum = Pattern.compile("^-?\\d+$");
    private Pattern floatNum = Pattern.compile("[+-]?(\\d+\\.\\d+|\\.\\d+|\\d+\\.)([eE]\\d+)?");
    private Pattern hexadecimalNum = Pattern.compile("^0[xX][0-9a-fA-F]+");
    private Pattern operator = Pattern.compile("^[+-/*<>=^!&|]*$");
    private Pattern comment = Pattern.compile("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)|(#.*)");
    private Pattern keyword = Pattern.compile("^abstract$|^and$|^array$|^as$|" +
            "^break$|^callable$|^case$|^catch$|^class$|^clone$|^const$|^continue$|^declare$|^default$|^die$|^do$|^echo$|^else$|^elseif$|" +
            "^empty$|^enddeclare$|^endfor$|^endforeach$|^endif$|^endswitch$|^endwhile$|^eval$|^exit$|^extends$|^final$|^finally$|^fn$|^for$|^foreach$|" +
            "^function$|^global$|^goto$|^if$|^implements$|^include$|^include_once$|^instanceof$|^insteadof$|^interface$|" +
            "^isset$|^list$|^match$|^namespace$|^new$|^or$|^print$|^private$|^protected$|^public$|^readonly$|^require$|" +
            "^switch$|^throw$|^trait$|^try$|^unset$|^use$|^var$|^while$|^xor$|^yield$|^yield from$|^return$");
    private Pattern delimiter = Pattern.compile("[,;:]");
    private Pattern brackets = Pattern.compile("[()\\[\\]{}]");
    private Pattern customString = Pattern.compile("\"");

    public String inputString;
    public ArrayList<String> allWords = new ArrayList();

    Lexer(String inputString) {
        this.inputString = inputString;
    }

    public void findAllLexemes() {
        ArrayList<String> temp = new ArrayList();
        ArrayList<String> temp1 = new ArrayList();
        String temp2 = "";
        int k = 0;
        for (int i = 0; i < inputString.length(); i++) {
            if (customString.matcher(Character.toString(inputString.charAt(i))).find()) {
                k++;
                if (k % 2 == 0) {
                    temp2 += inputString.charAt(i);
                    temp.add(temp2);
                    temp2 = "";
                } else {
                    temp.add(temp2);
                    temp2 = "";
                    temp2 += inputString.charAt(i);
                }
            } else if (delimiter.matcher(Character.toString(inputString.charAt(i))).find() ||
                    brackets.matcher(Character.toString(inputString.charAt(i))).find()) {
                temp.add(temp2);
                temp2 = "";
                temp2 += inputString.charAt(i);
                temp.add(temp2);
                temp2 = "";
            } else if ((inputString.charAt(i) == '/' && inputString.charAt(i + 1) == '/') ||
                    (inputString.charAt(i) == '/' && inputString.charAt(i + 1) == '*')) {
                temp.add(temp2);
                temp2 = "";
                temp2 += Character.toString(inputString.charAt(i)) + Character.toString(inputString.charAt(i + 1));
                i++;
            } else if (inputString.charAt(i) == '*' && inputString.charAt(i + 1) == '/') {
                temp2 += Character.toString(inputString.charAt(i)) + Character.toString(inputString.charAt(i + 1));
                temp.add(temp2);
                temp2 = "";
                i++;
            } else if (inputString.charAt(i) == '#') {
                temp.add(temp2);
                temp2 = "";
                temp2 += inputString.charAt(i);
            } else {
                temp2 += inputString.charAt(i);
                if (i == inputString.length() - 1) temp.add(temp2);
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) != "" && temp.get(i) != " " && temp.get(i) != null) {
                if (customString.matcher(temp.get(i)).find() || comment.matcher(temp.get(i)).find()) allWords.add(temp.get(i));
                else {
                    List<String> temp3 = Arrays.asList(temp.get(i).split("\s"));
                    for (int j = 0; j < temp3.size(); j++) {
                        if (temp3.get(j) != "" && temp3.get(j) != " " && temp3.get(j) != null) {
                            allWords.add(temp3.get(j));
                        }
                    }
                }
            }
        }
    }

    public void printType() {
        String word;
        for(int i = 0; i < allWords.size(); i++){
            word = allWords.get(i);
            if(identifier.matcher(word).find()) System.out.println(word + " IDENTIFIER");
            else if(hexadecimalNum.matcher(word).find()) System.out.println(word + " HEXADECIMAL NUMBER");
            else if(floatNum.matcher(word).find()) System.out.println(word + " FLOAT NUMBER");
            else if(decNum.matcher(word).find()) System.out.println(word + " DECIMAL NUMBER");
            else if(comment.matcher(word).find()) System.out.println(word + " COMMENT");
            else if(operator.matcher(word).find()) System.out.println(word + " OPERATOR");
            else if(keyword.matcher(word).find()) System.out.println(word + " KEY WORD");
            else if(delimiter.matcher(word).find()) System.out.println(word + " DELIMITER");
            else if(brackets.matcher(word).find()) System.out.println(word + " BRACKETS");
//            else if(name.matcher(word).find()) System.out.println(word + " CUSTOM NAME");
            else if(customString.matcher(word).find()) System.out.println(word + " CUSTOM STRING");
            else if(Objects.equals(allWords.get(i - 1), "function")) System.out.println(word + " CUSTOM NAME");
            else System.out.println(word + " INCORRECT LEXEME");
        }
    }
}
