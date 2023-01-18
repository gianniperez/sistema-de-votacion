package Sistema;
import java.util.*;

import Estructuras.Tupla;

public class MesaMayores extends Mesa {

    private int cupos;
    private int turnosAsignados;
    
    public MesaMayores(String tipo, Votante presidente, int numero) {
		super(tipo, presidente, numero);
		this.cupos = 10;
		this.turnosAsignados = 0;
		this.horaInicial = 8;
		this.horaFinal = 18;
	}

    public Tupla<Integer, Integer> reservarTurno(Votante v) {
		int hora = horaInicial + (turnosAsignados / cupos);
		Tupla<Integer, Integer> turno = new Tupla<Integer, Integer>(numero, hora);
		v.setTurno(turno);
		turnosAsignados++;
		agregarVotante(v);
		return turno;
    }

    public Boolean tieneCuposDisponibles() {
    	return turnosAsignados < (cupos) * (horaFinal - horaInicial);
    }

}
