package FX;

public class ObjectPassword {

    private String password;
    private String source;
    private String note;
    private String login;
    private String URL;

    public ObjectPassword(String password, String source, String login, String note, String URL) {
        this.password = password;
        this.source = source;
        this.note = note;
        this.login = login;
        this.URL = URL;

    }

    public ObjectPassword(String line) throws Exception {
        String[] split = line.split(":");
        this.password = split[0];
        this.source = split[1];
        this.note = split[2];
        this.login = split[3];
        this.URL = split[4];

    }

    public String getSaveFormat() {

        return password + "|" + source + "|" + login + "|" + note + "|" + URL;
    }

    public String getPassword() {

        return password;
    }

    public String getSource() {

        return source;
    }

    public String getNote() {
        return note;
    }

    public String getLogin() {
        return login;
    }

    public String getURL() {
        return URL;
    }
}
