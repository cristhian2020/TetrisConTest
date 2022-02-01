import java.awt.Color;import java.awt.Color; 

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;


public class Tetris extends JFrame {
	
	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = -4722429764792514382L;
                   
	/**
	 * La cantidad de milisegundos por cuadro.
	 */
	private static final long Tiempo = 1000L / 50L;
	
	/**
	 * La cantidad de piezas que existen.
	 */
	private static final int cantPiesa = Figura .values().length;
		
	/**
	 * La instancia de tablero del Panel.
	 */
	private Tablero tabl;
	
	/**
	 * La instancia de SidePanel.
	 */
	private PanelLateral lado;
	
	/**
	 * Si el juego está en pausa o no.
	 */
	private boolean Pausa;
	
	/**
	 * Ya sea que hayamos jugado un juego o no. Esto se establece en verdad
                        * inicialmente y luego se establece en falso cuando comienza el juego.
	 */
	private boolean nuevoJuego;
	
	private boolean GameOver;
	
	private int Nivel;
	
	private int Puntos;
	

	private Random random;
	
	private Reloj tiempoLogico;
	
	private Figura  FigActual;
	
	/**
	 * El siguiente tipo de figura
	 */
	private Figura  sigFig;
		

	private int colActl;
	
	
	private int filaAct;
	
	/**
	 * La rotación actual de nuestro mosaico.
	 */
	private int rotAct;
		
	/**
	 * Asegura que pase una cierta cantidad de tiempo después de que una pieza es
                       * generado antes de que podamos soltarlo.
	 */
	private int enfriarCaid;
	
	/**
	 * La velocidad del juego.
	 */
	private float velJuego;
	
	private Tetris() {
		/*
		 * Establezca las propiedades básicas de la ventana.
		 */
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		/*
		 *Inicialice las instancias de BoardPanel y SidePanel.
		 */
		this.tabl = new Tablero(this);
		this.lado = new PanelLateral(this);
		
		/*
		 *Agregue las instancias de BoardPanel y SidePanel a la ventana.
		 */
		add(tabl, BorderLayout.CENTER);
		add(lado, BorderLayout.EAST);
		
		/*
		 * Agrega un KeyListener anónimo personalizado al marco.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				switch(e.getKeyCode()) {
				
				/*
				 *Soltar: cuando se presiona, verificamos que el juego no esté
                                  * pausado y que no hay tiempo de reutilización desplegable, luego configure el
                                  * Temporizador lógico para funcionar a una velocidad de 25 ciclos por segundo.
				 */
				case KeyEvent.VK_S:
					if(!Pausa && enfriarCaid == 0) {
						tiempoLogico.setCyclesPerSecond(25.0f);
					}
					break;
					
				/*
				 * Mover a la izquierda: cuando se presiona, verificamos que el juego esté
                                                                                            * no pausado y que la posición a la izquierda de la corriente 
                                                                                            *  la posición es válida. Si es así, disminuimos la columna actual en 1.
				 */
				case KeyEvent.VK_A:
					if(!Pausa && tabl.esValida(FigActual, colActl - 1, filaAct, rotAct)) {
						colActl--;
					}
					break;
					
				/*
				 * Mover a la derecha: cuando se presiona, verificamos que el juego esté
                                                                                           * no pausado y que la posición a la derecha de la corriente
                                                                                           * la posición es válida. Si es así, incrementamos la columna actual en 1.
				 */
				case KeyEvent.VK_D:
					if(!Pausa && tabl.esValida(FigActual, colActl + 1, filaAct, rotAct)) {
						colActl++;
					}
					break;
					
				/*
				   *Girar en sentido antihorario: cuando se presiona, verifique que el juego no esté en pausa
                                                                                             * y luego intente girar la pieza en sentido antihorario. Debido al tamaño y
                                                                                             * complejidad del código de rotación, así como su similitud con el sentido de las agujas del reloj
                                                                                             * rotación, el código para rotar la pieza se maneja en otro método.
				 */
				case KeyEvent.VK_Q:
					if(!Pausa) {
						rotarPieza((rotAct == 0) ? 3 : rotAct - 1);
					}
					break;
				
				/*
			                      *Girar en el sentido de las agujas del reloj: cuando se presiona, verifique que el juego no esté en pausa
                                                 * y luego intente girar la pieza en el sentido de las agujas del reloj. Debido al tamaño y
                                                                                          * complejidad del código de rotación, así como su similitud con el sentido contrario a las agujas del reloj
                                                                                          *  Rotación, el código para rotar la pieza se maneja con otro método. 
				 */
				case KeyEvent.VK_E:
					if(!Pausa) {
						rotarPieza((rotAct == 3) ? 0 : rotAct + 1);
					}
					break;
					
				/*
				 * Pausar juego: cuando se presiona, compruebe que estamos jugando un juego.
                                                                                            * Si es así, cambie la variable de pausa y actualice el temporizador lógico para reflejar esto
                                                                                            * cambiar, de lo contrario, el juego ejecutará una gran cantidad de actualizaciones y esencialmente
                                                                                             * hacer que un juego instantáneo termine cuando salimos de la pausa si permanecemos en pausa durante más de un
                                                                                             * minuto más o menos.
				 */
				case KeyEvent.VK_P:
					if(!GameOver && !nuevoJuego) {
						Pausa = !Pausa;
						tiempoLogico.setPaused(Pausa);
					}
					break;
				
				/*
				 *Iniciar juego: cuando se presiona, verifique que estemos en un juego terminado o nuevo
                                                                                           * estado del juego. Si es así, reinicia el juego.
				 */
				case KeyEvent.VK_ENTER:
					if(GameOver || nuevoJuego) {
						reiniciarJuego();
					}
					break;
				
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				/** Caída: cuando se suelta, configuramos la velocidad del temporizador lógico
                                   *volver a la velocidad actual del juego y borrar
                                   * cualquier ciclo que aún pueda haber transcurrido.
				 */
				case KeyEvent.VK_S:
					tiempoLogico.setCyclesPerSecond(velJuego);
					tiempoLogico.reset();
					break;
				}
				
			}
			
		});
		
		/*
		 * Aquí cambiamos el tamaño del marco para contener las instancias de BoardPanel y SidePanel,
                                               * Centrar la ventana en la pantalla y mostrarla al usuario.
		 */
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Inicia el juego en ejecución. Inicializa todo y entra en el bucle del juego.
	 */
	private void iniciarJuego() {
		/*
		 * Inicialice nuestro generador de números aleatorios, temporizador lógico y nuevas variables de juego.
		 */
		this.random = new Random();
		this.nuevoJuego = true;
		this.velJuego= 1.0f;
		
		/*
		 * Configure el temporizador para evitar que el juego se ejecute antes de que el usuario presione enter
                                              * para iniciarlo.
		 */
		this.tiempoLogico = new Reloj(velJuego);
		tiempoLogico.setPaused(true);
		
		while(true) {
			//Obtenga la hora en que comenzó el marco.
			long start = System.nanoTime();
			
			//Actualice el temporizador lógico.
			tiempoLogico.update();
			
			/*
			 * Si ha transcurrido un ciclo en el temporizador, podemos actualizar el juego y
                                                                    * mover nuestra pieza actual hacia abajo.
			 */
			if(tiempoLogico.hasElapsedCycle()) {
				actualizarJuego();
			}
		
			//Disminuya el enfriamiento de la gota si es necesario.
			if(enfriarCaid > 0) {
				enfriarCaid--;
			}
			
			//Mostrar la ventana al usuario.
			renderJuego();
			
			/*
			 * Duerme para limitar la velocidad de fotogramas.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < Tiempo) {
				try {
					Thread.sleep(Tiempo - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Actualiza el juego y maneja la mayor parte de su lógica.
	 */
	private void actualizarJuego() {
		/*
		 * Verifique si la posición de la pieza se puede mover hacia la siguiente fila.
		 */
		if(tabl.esValida(FigActual,colActl,filaAct + 1, rotAct)) {
			//Incrementa la fila actual si es seguro hacerlo.
			filaAct++;
		} else {
			/*
			 * Hemos llegado al final del tablero o hemos aterrizado en otra pieza, así que
                                                                    * Necesitamos agregar la pieza al tablero.
			 */
			tabl.addPiesa(FigActual, colActl, filaAct, rotAct);
			
			/*
			 *Verifique si agregar la nueva pieza resultó en líneas despejadas. Si es así,
                                                                    * aumentar la puntuación del jugador. (Se pueden borrar hasta 4 líneas de una sola vez;
                                                                    * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
			int despejado = tabl.revLineas();
			if(despejado > 0) {
				Puntos += 50 << despejado;
			}
			
			/*
			 * Aumenta ligeramente la velocidad para la siguiente pieza y actualiza el temporizador del juego.
                          * para reflejar el aumento.
			 */
			velJuego += 0.035f;
			tiempoLogico.setCyclesPerSecond(velJuego);
			tiempoLogico.reset();
			
			/*
			 * Configura el tiempo de reutilización para que la siguiente pieza no salga volando automáticamente
                                                                     * desde los cielos inmediatamente después de que llegue esta pieza si no hemos reaccionado
                                                                     * todavía. (~ 0,5 segundos de búfer). 
			 */
			enfriarCaid = 25;
			
			/*
			 * Actualiza el nivel de dificultad. Esto no tiene ningún efecto en el juego y solo es
                                                                    * utilizado en la cadena "Nivel" en el SidePanel.
			 */
			Nivel = (int)(velJuego * 1.70f);
			
			/*
			 * Genera una nueva pieza para controlar.
			 */
			aparecerPieza();
		}		
	}
	
	/**
	 * Obliga a tablero y panel lateral a volver a pintar.
	 */
	private void renderJuego() {
		tabl.repaint();
		lado.repaint();
	}
	
	/**
	 * Restablece las variables del juego a sus valores predeterminados al principio.
                       * de un nuevo juego.
	 */
	 private void reiniciarJuego() {
		this.Nivel = 1;
		this.Puntos = 0;
		this.velJuego = 1.0f;
		this. sigFig = Figura .values()[random.nextInt(cantPiesa)];
		this.nuevoJuego = false;
		this.GameOver = false;		
		tabl.Limpiar();
		tiempoLogico.reset();
		tiempoLogico.setCyclesPerSecond(velJuego);
		aparecerPieza();
	}
		
	/**
	 * Genera una nueva pieza y restablece las variables de nuestra pieza a sus valores predeterminados
                       * valores.
	 */
	 private void aparecerPieza() {
		/*
		 * Sondear la última pieza y restablecer nuestra posición y rotación a
                                              * sus variables predeterminadas, luego elija la siguiente pieza para usar.
		 */
		this.FigActual =  sigFig;
		this.colActl = FigActual.getApareserColumn();
		this.filaAct = FigActual.getApareserFil();
		this.rotAct= 0;
		this. sigFig = Figura .values()[random.nextInt(cantPiesa)];
		
		/*
		 * Si el punto de generación no es válido, debemos pausar el juego y marcar que hemos perdido
                                              * porque significa que las piezas del tablero se han elevado demasiado.
		 */
		if(!tabl.esValida(FigActual, colActl, filaAct, rotAct)) {
			this.GameOver = true;
			tiempoLogico.setPaused(true);
		}		
	}

	/**
	 *Intenta establecer la rotación de la pieza actual en newRotation.
	 */
	private void rotarPieza(int nuevaRotacion) {
		/*
		 * A veces, las piezas deberán moverse cuando se giran para evitar recortes
                   * fuera del tablero (la pieza I es un buen ejemplo de esto). Aquí almacenamos
                   * una fila y columna temporales en caso de que necesitemos mover el mosaico también.
		 */
		int nuevaColumna = colActl;
		int nuevaFila =filaAct;
		
		/*
		 * Obtenga los insertos para cada uno de los lados. Estos se utilizan para determinar cómo
                                              * muchas filas o columnas vacías hay en un lado dado.
		 */
		int Izquierda = FigActual.getRecuadroIzq(nuevaRotacion);
		int derecha = FigActual.getRecuadroDer(nuevaRotacion);
		int top = FigActual.getRecuadroSupe(nuevaRotacion);
		int fondo = FigActual.getRecuadroInferior(nuevaRotacion);
		
		/*
		 * Si la pieza actual está demasiado hacia la izquierda o hacia la derecha, aleje la pieza de los bordes
                  * para que la pieza no se salga del mapa y pierda su validez automáticamente.
		 */
		if(colActl < -Izquierda) {
			nuevaColumna -= colActl - Izquierda;
		} else if(colActl+FigActual.getDimension() - derecha >= Tablero.numCol) {
			nuevaColumna -= (colActl + FigActual.getDimension() - derecha) -Tablero.numCol + 1;
		}
		
		/*
		 * Si la pieza actual está demasiado lejos hacia arriba o hacia abajo, aleje la pieza de los bordes
                                              * para que la pieza no se salga del mapa y pierda su validez automáticamente.
		 */
		if(filaAct < -top) {
			nuevaFila -= filaAct - top;
		} else if(filaAct + FigActual.getDimension() - fondo >= Tablero.TotalFilas) {
			nuevaFila -= (filaAct + FigActual.getDimension() - fondo) - Tablero.TotalFilas + 1;
		}
		
		/*
		 * Verifique si la nueva posición es aceptable. Si es así, actualice la rotación y
                                              * posición de la pieza.
        		 */
		if(tabl.esValida(FigActual, nuevaColumna, nuevaFila, nuevaRotacion)) {
			rotAct = nuevaRotacion;
			filaAct = nuevaFila;
			colActl = nuevaColumna;
		}
	}
	
	/**
	 * Comprueba si el juego está en pausa o no.
	 */
	public boolean Pausado() {
		return Pausa;
	}
	
	/**
	 * Comprueba si el juego ha terminado o no.
	 */
	public boolean GameOver() {
		return GameOver;
	}
	
	/**
	 * Comprueba si estamos o no en un juego nuevo.
	 */
	public boolean NuevoJuego() {
		return nuevoJuego;
	}
	
	/**
	 * Obtiene la puntuación actual.
	 */
	public int getPuntos() {
		return Puntos;
	}
	
	/**
	 * Obtiene el nivel actual.
	 */
	public int getNivel() {
		return Nivel;
	}
	
	/**
	 * Obtiene el tipo actual de figura que estamos usando.
	 */
	public Figura  getFigAct() {
		return FigActual;
	}
	
	/**
	 *Obtiene el siguiente tipo de figura que estamos usando.
	 */
	public Figura  getSigFig() {
		return  sigFig;
	}
	
	/**
	 *Obtiene la columna de la figura actual.
	 */
	public int getColFig() {
		return colActl;
	}
	
	/**
	 * Obtiene la fila de la figura actual.
	 */
	public int getFilAct() {
		return filaAct;
	}
	
	/**
	 * Obtiene la rotación de la figura actual.
	 */
	public int getRotaFig() {
		return rotAct;
	}

	/**
	* Punto de entrada al juego. Responsable de crear e iniciar un nuevo
                       * instancia de juego.
	 */
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.iniciarJuego();
	}

}
