package Sistema;
import java.util.*;
import java.util.Map.Entry;

import Estructuras.Tupla;

public abstract class Mesa {
	
	private String tipo;
    private Votante presidente;
    protected int numero;
	private Hashtable<Integer, Votante> votantesAsignados;
    protected int horaInicial;
    protected int horaFinal;
    
    public Mesa(String tipo, Votante presidente, int numero) {
    	this.tipo = tipo;
		this.presidente = presidente;
		this.numero = numero;
		votantesAsignados = new Hashtable<Integer, Votante>();
	}

    public Votante consultarPresidente() {
        return presidente;
    }
    
    public void agregarVotante(Votante v) {
    	votantesAsignados.put(v.getDni(), v);
    }
    
    public abstract Tupla<Integer, Integer> reservarTurno(Votante v);
    
    public abstract Boolean tieneCuposDisponibles();

	public int getNumero() {
		return numero;
	}

	public Boolean esDeEnfYMayor() {
		return tipo.equals("Enf_Preex") || tipo.equals("Mayor65");
	}
	
	public Boolean esDeTrabajadores() {
		return tipo.equals("Trabajador");
	}
	
	public Boolean esDeEnfermos() {
		return tipo.equals("Enf_Preex");
	}
	
	public Boolean esDeMayores() {
		return tipo.equals("Mayor65");
	}
	
	public Boolean esGeneral() {
		return tipo.equals("General");
	}

	public Hashtable<Integer, Votante> getVotantesAsignados() {
		return votantesAsignados;
	}

	public Map<Integer, List<Integer>> dameVotantesPorHorario() {
		Map<Integer, List<Integer>> lista = new HashMap<Integer, List<Integer>>();
		Set<Entry<Integer, Votante>> votantesDeLaMesa = this.votantesAsignados.entrySet();
		if (votantesDeLaMesa.size() == 0) {
			return null;
		}
		Iterator<Entry<Integer, Votante>> it = votantesDeLaMesa.iterator();
		for (int j = this.horaInicial; j < this.horaFinal; j++) {
			it = votantesDeLaMesa.iterator(); // reinicio el iterador
			LinkedList<Integer> listaDni = new LinkedList<Integer>();
			while (it.hasNext()) {
				Votante v = it.next().getValue();
				Tupla<Integer, Integer> turno = v.consultarTurno();
				if (turno.getY().equals(j)) {
					listaDni.add(v.getDni());
				}
			}
			lista.put(j, listaDni);
		}
		return lista;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Mesa))
			return false;
		Mesa other = (Mesa) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mesa [Número: " + numero + ". Presidente: " + presidente.getNombre() + ". Tipo: " + tipo + "]";
	}
	
}

