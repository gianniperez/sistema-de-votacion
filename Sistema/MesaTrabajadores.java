package Sistema;
import java.util.*;

import Estructuras.Tupla;

public class MesaTrabajadores extends Mesa {

    public MesaTrabajadores(String tipo, Votante presidente, int numero) {
		super(tipo, presidente, numero);
		this.horaInicial = 8;
		this.horaFinal = 12;
	}

	public Tupla<Integer, Integer> reservarTurno(Votante v) {
		Random r = new Random();
		int hora = horaInicial + r.nextInt(4);
		Tupla<Integer, Integer> turno = new Tupla<Integer, Integer>(numero,hora);
		v.setTurno(turno);
		agregarVotante(v);
		return turno;
    }

	public Boolean tieneCuposDisponibles() {
		return true;
	}

}
