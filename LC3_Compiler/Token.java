import java.util.regex.Pattern;

class Token {

    private static final Pattern REGISTER = Pattern.compile("[Rr][0-7]");
    private static final Pattern LITERAL = Pattern.compile("[#]-?[\\d]{1,5}|[xX][\\p{XDigit}]{1,4}|[bB][01]{1,16}");
    private static final Pattern OPCODE = Pattern.compile("(add)|(and)|(br[n]?[z]?[p]?)|(jmp)|(jsr)|(jsrr)|(ld)|" +
            "(ldi)|(ldr)|(lea)|(not)|(ret)|(rti)|(st)|(sti)|(str)|(trap)|(mul)|(sub)");
    private static final Pattern DIRECTIVE = Pattern.compile("\\.(orig|fill|blkw|stringz|end)");
    private static final Pattern VECTOR = Pattern.compile("(getc)|(out)|(puts)|(in)|(putsp)|(halt)");
    private static final Pattern STRING = Pattern.compile("\"[.]*\"");
    private static final Pattern LABEL = Pattern.compile("[a-zA-Z][\\S]{0,19}");

    Type type;
    String info;

    enum Type {
        REGISTER,
        LITERAL,
        OPCODE,
        DIRECTIVE,
        VECTOR,
        STRING,
        LABEL,
        ERROR
    }

    private Token(Type type, String info) {
        this.type = type;
        this.info = info;
    }

    static Token tokenize(String str) {
        if (REGISTER.matcher(str).matches()) {
            return new Token(Type.REGISTER, str.substring(1));
        } else if (LITERAL.matcher(str).matches()) {
            String leading = str.substring(0,1);
            String value;
            if (leading.equals("X") || leading.equals("x")) {
                value = String.valueOf(Integer.parseInt(str.substring(1),16));
            } else if (leading.equals("B") || leading.equals("b")) {
                value = String.valueOf(Integer.parseInt(str.substring(1),2));
            } else {
                value = str.substring(1);
            }
            return new Token(Type.LITERAL, value);
        } else if (OPCODE.matcher(str.toLowerCase()).matches()) {
            str = str.toLowerCase();
            return new Token(Type.OPCODE, str.toLowerCase());
        } else if (DIRECTIVE.matcher(str.toLowerCase()).matches()) {
            str = str.toLowerCase();
            return new Token(Type.DIRECTIVE, str.toLowerCase());
        } else if (VECTOR.matcher(str.toLowerCase()).matches()) {
            str = str.toLowerCase();
            return new Token(Type.VECTOR, str.toLowerCase());
        } else if (STRING.matcher(str).matches()){
            return new Token(Type.STRING, str.replace("\"", ""));
        } else if (LABEL.matcher(str).matches()) {
            return new Token(Type.LABEL, str);
        } else {
            return new Token(Type.ERROR, str);
        }
    }

    @Override
    public String toString() {
        String chr = "ERR";
        if (type == Type.REGISTER) {
            chr = "R";
        } else if (type == Type.LITERAL) {
            chr = "N";
        } else if (type == Type.OPCODE) {
            chr = "O";
        } else if (type == Type.DIRECTIVE) {
            chr = "D";
        } else if (type == Type.VECTOR) {
            chr = "V";
        } else if (type == Type.STRING) {
            chr = "S";
        } else if (type == Type.LABEL) {
            chr = "L";
        }
        return chr;
    }
}