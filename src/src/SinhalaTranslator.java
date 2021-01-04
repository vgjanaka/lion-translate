/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author Janaka Pathma Kumara
 */
public class SinhalaTranslator {

    private StringBuffer buffer;
    private int editPosition;
    private int editLength;
    private int cursorPosition;
    private int editPos;
    private int replacedLength;
    private String translatedText;
    private static final int repaya = 8;
    private static final int rakaranshaya = 20;
    private static final int binduva = 42;
    private static final char englishAkshara[][] = {
        {'a', 'e', 'e'}, {'a', 'e'}, {'a', 'a'}, {'a', 'u'}, {'a'},
        {'i', 'i'}, {'i', 'r', 'u', 'u'}, {'i', 'r', 'u'}, {'i', 'l', 'u', 'u'}, {'i', 'l', 'u'}, {'i', 'l'},
        {'i', 'r'}/* Empty */, {'i'},
        {'u', 'u'}, {'u'}, {'e', 'e'}, {'e', 'i'},
        {'e'}, {'o', 'o'}, {'o'}, {'z'}
    };
    private static final char sinhalaAkshara[][] = {
        {'\u0D88'}, {'\u0D87'}, {'\u0D86'}, {'\u0D96'}, {'\u0D85'},
        {'\u0D8A'}, {'\u0D8E'}, {'\u0D8D'}, {'\u0D90'}, {'\u0D8F'}, {'i', 'l'},
        {'i', 'r'}, {'\u0D89'}, {'\u0D8C'}, {'\u0D8B'}, {'\u0D92'}, {'\u0D93'},
        {'\u0D91'}, {'\u0D95'}, {'\u0D94'}, {'\u0DF4'}
    };
    private static final char englishViyanjana[][] = {
        {'k', '-', 'D', 'H'}, {'k', '-', 'd', 'h'}, {'h', '-', 'd', 'h'}, {'k'}, {'K'}, {'G'},
        {'X'}, {'g', 'A'}, {'g'}, {'c', 'h'}, {'C', 'H'}, {'j', 'A'},
        {'j'}, {'t', 'h'}, {'T', 'H'}, {'t'}, {'T', 'A'}, {'T'}, {'d', 'h', 'A'},
        {'d', 'h'}, {'d'}, {'D', 'A'}, {'D', 'H'}, {'D'}, {'N'},
        {'n'}, {'y'}, {'r'}, {'l'}, {'v'}, {'s', 'h'},
        {'S', 'H'}, {'s'}, {'h'}, {'L'}, {'f'}, {'p'},
        {'P'}, {'b', 'A'}, {'b'}, {'B'}, {'m'}, {'x'}
    };
    private static final char sinhalaViyanjana[][] = {
        {'\u0DA3'}, {'\u0DA4'}, {'\u0DA5'}, {'\u0D9A'}, {'\u0D9B'}, {'\u0D9D'},
        {'\u0D9E'}, {'\u0D9F'}, {'\u0D9C'}, {'\u0DA0'}, {'\u0DA1'}, {'\u0DA6'},
        {'\u0DA2'}, {'\u0DAD'}, {'\u0DAE'}, {'\u0DA7'}, {'\u0DAA'}, {'\u0DA8'}, {'\u0DB3'},
        {'\u0DAF'}, {'\u0DA9'}, {'\u0DAC'}, {'\u0DB0'}, {'\u0DB0'}, {'\u0DAB'},
        {'\u0DB1'}, {'\u0DBA'}, {'\u0DBB'}, {'\u0DBD'}, {'\u0DC0'}, {'\u0DC1'},
        {'\u0DC2'}, {'\u0DC3'}, {'\u0DC4'}, {'\u0DC5'}, {'\u0DC6'}, {'\u0DB4'},
        {'\u0DB5'}, {'\u0DB9'}, {'\u0DB6'}, {'\u0DB7'}, {'\u0DB8'}, {'\u0D82'}
    };
    private static final char englishAdditional[][] = {
        {'a', 'a'}, {'a', 'e', 'e'}, {'a', 'e'}, {'a', 'o'}, {'i', 'i'}, {'i'},
        {'u', 'u'}, {'u'}, {'r', 'r'}, {'r', 'u', 'u'}, {'r', 'u'}, {'e', 'e'},
        {'e'}, {'y', 'i'}, {'o', 'o'}, {'o'}, {'a', 'u', 'u'}, {'a', 'u'},
        {'x', 'x'}, {'x'}, {'r'}, {'Y'}, {'y'}
    };
    private static final char sinhalaAdditional[][] = {
        {'\u0DCF'}, {'\u0DD1'}, {'\u0DD0'}, {'\u0DF3'}, {'\u0DD3'}, {'\u0DD2'}, {'\u0DD6'},
        {'\u0DD4'}, {'\u0DBB', '\u0DCA', '\u200D'}, {'\u0DF2'}, {'\u0DD8'}, {'\u0DDA'}, {'\u0DD9'},
        {'\u0DDB'}, {'\u0DDD'}, {'\u0DDC'}, {'\u0DDF'}, {'\u0DDE'}, {'\u0D83'},
        {'\u0D82'}, {'\u0DCA', '\u200D', '\u0DBB'}, {'\u0DCA', '\u200D', '\u0DBA'}, {'y'}
    };

    public SinhalaTranslator() {
        this("");
    }

    public SinhalaTranslator(String str) {
        buffer = new StringBuffer();
        appendText(str);
    }

    public String appendText(String str) {

        return insertText(str, buffer.length());
    }

    public String insertText(String str, int pos) {

        int prevPos = pos;

        //Is it inserting out to the editing position
        if (editPosition != -1 && !(editPosition <= pos && pos < editPosition + editLength)) {
            if (editPosition > pos) {
                translateAll();
            } else {
                prevPos = editPosition;
            }
        }

        setReplacedPosition(prevPos);
        setReplacedLength(pos - prevPos);
        int prevEditPosition = editPosition;
        int prevEditLength = editLength;

        if (pos == buffer.length()) {
            buffer.append(str);
        } else {
            buffer.insert(pos, str);
        }

        String str2;

        str2 = translate(buffer, prevPos, pos + str.length(), true);
        setTranslatedText(str2);

        if (prevEditPosition == -1) {
            buffer.replace(pos, pos + str.length(), str2);
            setCursorPosition(pos + str2.length());
        } else {
            buffer.replace(prevPos, prevPos + prevEditLength + str.length(), str2);
            setCursorPosition(prevPos + str2.length());
        }

        return str2;
    }

    //There are six cases with deleting... if there are non translated text those should translate or it needs
    //to change edit positions.
    public void deleteText(int from, int length) {

        int to = from + length;

        buffer.delete(from, to);
        int editFrom = editPosition;
        int editTo = editFrom + editLength;
        boolean translate = false;

        if (editPosition != -1) {
            if (to <= editFrom) {//1

                editFrom -= length;
                editTo -= length;
                editPosition -= length;

                if (to != editFrom) {
                    translate = true;
                }

            } else if (editTo <= from) {//2
                translate = true;
            } else if (from <= editFrom && editTo <= to) {//6
                editPosition = -1;
            } else if (from <= editFrom && to > editFrom && to < editTo) {//3
                editLength -= (to - editFrom);
                editPosition = from;
                translate = true;
            } else if (from >= editFrom && from < editTo && to >= editTo) {//4
                editLength -= (editTo - from);
            } else if (editFrom <= from && to <= editTo) {//5
                editLength -= (to - from);
            }

            if (editLength == 0) {
                editPosition = -1;
            }
        }

        if (translate) {
            translateAll();
        } else {
            setReplacedPosition(0);
            setReplacedLength(0);
            setTranslatedText("");
            cursorPosition = buffer.length();
        }

        setCursorPosition(from);
    }

    public void translateAll() {

        if (editPosition != -1) {
            int prevEditPosition = editPosition;
            int prevEditLength = editLength;
            String str = translate(buffer, editPosition, editPosition + editLength, false);
            setTranslatedText(str);
            buffer.replace(prevEditPosition, prevEditPosition + prevEditLength, str);
            setReplacedPosition(prevEditPosition);
            setReplacedLength(prevEditLength);
            editPosition = -1;
            return;
        }

        setReplacedPosition(0);
        setReplacedLength(0);
        setTranslatedText("");
        cursorPosition = buffer.length();
    }

    public String translate(String str, int from, int to, boolean partialEdit) {
        return translate(new StringBuffer(str), from, to, partialEdit);
    }

    private String translate(StringBuffer str, int from, int to, boolean partialEdit) {

        StringBuilder ret = new StringBuilder();

        char c;
        String appendStr = null;

        for (int i = from; i < to;) {

            int pos;
            int initialPoint = from + ret.length();
            int intialPointEng = i;
            appendStr = "";
            c = str.charAt(i);

            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) || (c == '-')) {

                if (c == '|') {
                    if (i + 1 < to && str.charAt(i + 1) == '|') {
                        appendStr += '|';
                        i += 2;
                    } else {
                        i++;
                    }
                } else if (c == '-') {
                    if (i + 1 < to && str.charAt(i + 1) == '-') {
                        appendStr += '-';
                        i += 2;
                    } else {
                        i++;
                    }
                } else {
                    appendStr += c;
                    i++;
                }

            } else {

                if ((pos = findMathcingChar(str, englishViyanjana, i, to, false)) != -1) {

                    appendStr += new String(sinhalaViyanjana[pos]);
                    i += englishViyanjana[pos].length;

                    if (i < to) {
                        if (str.charAt(i) == '|' && (i + 1 < to && str.charAt(i + 1) != '|')) {
                            i++;
                        } else {

                            if (str.charAt(i) == '-' && (i + 1 >= to || (i + 1 < to && str.charAt(i + 1) != '-'))) {

                                int length;

                                if ((pos = findMathcingChar(str, englishViyanjana, i + 1, to, false)) != -1) {
                                    i++;//to skip '-'
                                    appendStr += "\u0DCA\u200D";
                                    appendStr += new String(sinhalaViyanjana[pos]);
                                    i += englishViyanjana[pos].length;
                                } else if ((length = isSubset(str, englishViyanjana, i + 1, to, false)) != -1) {
                                    i++;//to skip
                                    i += length;
                                }
                            }

                            int viyanjanaPos = pos;

                            if ((pos = findMathcingChar(str, englishAdditional, i, to, true)) != -1) {

                                if (pos == repaya) {
                                    appendStr = "\u0DBB\u0DCA\u200D" + appendStr;
                                } else {
                                    appendStr += new String(sinhalaAdditional[pos]);
                                }

                                i += englishAdditional[pos].length;

                                if (pos == rakaranshaya) {
                                    if ((pos = findMathcingChar(str, englishAdditional, i, to, true)) != -1) {

                                        if (pos == repaya) {
                                            appendStr = "\u0DBB\u0DCA\u200D" + appendStr;
                                        } else {
                                            appendStr += new String(sinhalaAdditional[pos]);
                                        }

                                        i += englishAdditional[pos].length;
                                    } else if (i < to && (str.charAt(i) == 'a' || str.charAt(i) == 'A')) {
                                        i++;
                                    }
                                }

                            } else if (i < to && str.charAt(i) == '-' && i + 1 < to && str.charAt(i + 1) == '-') {
                                appendStr += "-";
                                i++;
                            } else if (viyanjanaPos != binduva && (i == to || (i < to && !(str.charAt(i) == 'a' || str.charAt(i) == 'A' || str.charAt(i) == '-')))) {
                                appendStr += '\u0DCA';
                            } else if (i < to && (str.charAt(i) == 'a' || str.charAt(i) == 'A' || (str.charAt(i) == '-'))) {
                                i++;
                            }
                        }
                    }
                } else if ((pos = findMathcingChar(str, englishAkshara, i, to, false)) != -1) {
                    appendStr += new String(sinhalaAkshara[pos]);
                    i += englishAkshara[pos].length;
                } else if (str.charAt(i) == '-' && i + 1 < to && str.charAt(i + 1) == '-') {
                    appendStr += "-";
                    i++;
                } else {
                    appendStr += str.charAt(i);
                    i++;
                }
            }

            if ((i == to && partialEdit)) {
                editPosition = initialPoint;
                appendStr = str.substring(intialPointEng, to);
                editLength = appendStr.length();
            } else {
                editPosition = -1;
                editLength = 0;
            }

            ret.append(appendStr);
        }

        return ret.toString();
    }

    public static String translate(String str) {

        SinhalaTranslator s = new SinhalaTranslator();
        return s.translate(str, 0, str.length(), false);
    }

    private int findMathcingChar(StringBuffer buffer, char[][] englishArray, int from, int to, boolean ignoreCase) {

        for (int j = 0; j < englishArray.length; j++) {

            boolean found = true;
            int k = 0;

            for (k = 0; k < englishArray[j].length && from + k < to; k++) {
                if (englishArray[j][k] != buffer.charAt(from + k)) {

                    if (ignoreCase && ((englishArray[j][k] + 'A' - 'a') == buffer.charAt(from + k))) {
                        continue;
                    }

                    found = false;
                    break;
                }
            }

            if (found && k == englishArray[j].length) {
                return j;
            }
        }

        return -1;
    }

    private int isSubset(StringBuffer buffer, char[][] englishArray, int from, int to, boolean ignoreCase) {


        for (int j = 0; j < englishArray.length; j++) {

            boolean found = true;
            int k = 0;

            for (k = 0; k < englishArray[j].length && from + k < to; k++) {
                if (englishArray[j][k] != buffer.charAt(from + k)) {

                    if (ignoreCase && ((englishArray[j][k] + 'A' - 'a') == buffer.charAt(from + k))) {
                        continue;
                    }

                    found = false;
                    break;
                }
            }

            if (found) {
                return k;
            }
        }

        return -1;
    }

    //Entire translated text
    public String getText() {
        return buffer.toString();
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {

        if (cursorPosition < 0) {
            return;
        }

        this.cursorPosition = cursorPosition;
    }

    //replaced position by append, or insert or delete
    public int getReplacedPosition() {
        return editPos;
    }

    public void setReplacedPosition(int editPos) {
        this.editPos = editPos;
    }

    //replaced length by append, or insert or delete
    public int getReplacedLength() {
        return replacedLength;
    }

    public void setReplacedLength(int replacedLength) {
        this.replacedLength = replacedLength;
    }

    //Most recently translated part
    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    @Override
    public String toString() {
        return getText();
    }
}