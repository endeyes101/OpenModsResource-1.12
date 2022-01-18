package foxahead.simpleworldtimer;

public class Formatter
{
    private static String text;
    private static int length;
    private static int i;
    private static char c;
    
    public static String format(final String parText, final long w, final long t, final long d, final long dd, final long yy, final String subSeasonName, final String seasonName) {
        Formatter.text = parText;
        Formatter.length = parText.length();
        Formatter.i = -1;
        final StringBuilder out = new StringBuilder(Formatter.length * 2);
        final StringBuilder part = new StringBuilder();
        StringBuilder buffer = out;
        boolean nonZero = false;
        boolean hasValue = false;
        try {
            while (true) {
                getNextChar();
                if (Formatter.c == '[') {
                    part.setLength(0);
                    buffer = part;
                    nonZero = false;
                    hasValue = false;
                }
                else if (Formatter.c == ']') {
                    if (!hasValue) {
                        out.append("[");
                    }
                    if (!hasValue || nonZero) {
                        out.append((CharSequence)part);
                    }
                    if (!hasValue) {
                        out.append("]");
                    }
                    buffer = out;
                }
                else if (Formatter.c == '&') {
                    getNextChar();
                    switch (Formatter.c) {
                        case 'w': {
                            buffer.append(Long.toString(w));
                            nonZero |= (w != 0L);
                            hasValue = true;
                            continue;
                        }
                        case 't': {
                            buffer.append(String.format("%02d", t));
                            nonZero |= (t != 0L);
                            hasValue = true;
                            continue;
                        }
                        case 'd': {
                            buffer.append(Long.toString(d));
                            nonZero |= (d != 0L);
                            hasValue = true;
                            continue;
                        }
                        case 'D': {
                            buffer.append(Long.toString(dd));
                            nonZero |= (dd != 0L);
                            hasValue = true;
                            continue;
                        }
                        case 'Y': {
                            buffer.append(Long.toString(yy));
                            nonZero |= (yy != 0L);
                            hasValue = true;
                            continue;
                        }
                        case 'B': {
                            buffer.append(subSeasonName);
                            hasValue = true;
                            continue;
                        }
                        case 'S': {
                            buffer.append(seasonName);
                            hasValue = true;
                            continue;
                        }
                    }
                }
                else {
                    buffer.append(Formatter.c);
                }
            }
        }
        catch (Exception e) {
            return buffer.toString();
        }
    }
    
    private static void getNextChar() throws Exception {
        ++Formatter.i;
        if (Formatter.i < Formatter.length) {
            Formatter.c = Formatter.text.charAt(Formatter.i);
            return;
        }
        throw new Exception();
    }
}
