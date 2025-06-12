package sjt.solar.a3.eventos;

public class Evento {
    private int idEvento; // Adicionando um ID para identificar o evento
    private String nomeEvento;
    private String nomeLista;
    private String descricao;
    private String data;
    private String hora;
    private String local;

    public Evento(int idEvento, String nomeEvento, String nomeLista, String descricao, String data, String hora, String local) {
        this.idEvento = idEvento; // Inicializando o ID do evento
        this.nomeEvento = nomeEvento;
        this.nomeLista = nomeLista;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.local = local;
    }

    // Construtor sem ID - usado para novos eventos (ID será gerado pelo banco)
    public Evento(String nomeEvento, String nomeLista, String descricao, String data, String hora, String local) {
        this.nomeEvento = nomeEvento;
        this.nomeLista = nomeLista;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.local = local;
    }

    // Getters para acessar os atributos
    public int getIdEvento() {
        return idEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getLocal() {
        return local;
    }

    // setters para permitir a modificação dos atributos

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
