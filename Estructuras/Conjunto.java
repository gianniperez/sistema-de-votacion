package Estructuras;

import java.util.ArrayList;

public class Conjunto <T> {
	private ArrayList<T> elementos;
	private int posicion;
	
	public Conjunto() {
		elementos = new ArrayList<>();
		posicion = 0;
	}
	
	public void agregarElemento (T elemento) {
		if (! pertenece(elemento)) {
			elementos.add(elemento);
		}
	}
	
	public void quitarElemento (T elemento) {
		if (pertenece(elemento)) {
			posicion--;
			elementos.remove(elemento);
		}
	}
	
	public int cantElementos () {
		return elementos.size();
	}
	
	public boolean estaVacio () {
		return cantElementos() == 0;
	}
	
	public boolean pertenece (T elemento) {
		return elementos.contains(elemento);
	}
	
	public T dameElemento () {
		if (estaVacio()) {
			throw new RuntimeException("El conjunto está vacío");
		}
		
		
		
		if (posicion >= cantElementos()) {
			posicion = 0;
		}
		
		T pos = elementos.get(posicion);
		posicion++;
		return pos;
	}
	
	// destructiva
	public void union (Conjunto<T> conj2) {
		for (int i = 0; i < conj2.cantElementos(); i++) {
			T elemento = conj2.dameElemento();
			if (!pertenece(elemento))
			this.agregarElemento(elemento);
		}
	}
	
	public Conjunto<T> union2 (Conjunto<T> conj2) {
		Conjunto <T> nuevo = new Conjunto <T>();
		for (int i = 0; i < this.cantElementos(); i++) {
			T elemento = this.dameElemento();
			if (!nuevo.pertenece(elemento))
			nuevo.agregarElemento(elemento);
		}
		for (int i = 0; i < conj2.cantElementos(); i++) {
			T elemento = conj2.dameElemento();
			if (!nuevo.pertenece(elemento))
			nuevo.agregarElemento(elemento);
		}
		return nuevo;
	}
	
	// destructiva
	public void interseccion (Conjunto<T> conj2) {
		for (int i = 0; i < this.cantElementos(); i++) {
			T elemento = this.dameElemento();
			if (!conj2.pertenece(elemento))
			this.quitarElemento(elemento);
		}
	}
	
	public Conjunto<T> interseccion2 (Conjunto<T> conj2) {
		Conjunto <T> nuevo = new Conjunto <T>();
		for (int i = 0; i < this.cantElementos(); i++) {
			T elemento = this.dameElemento();
			if (this.pertenece(elemento) && conj2.pertenece(elemento))
			nuevo.agregarElemento(elemento);
		}
		return nuevo;
	}
	
	public String toString() {
		return elementos.toString();
	}
}
