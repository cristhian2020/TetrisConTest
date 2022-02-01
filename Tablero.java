 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Tablero extends JPanel {
	
	//mostrar las sombras en las baldosas
	public static final int COLOR_MIN = 35;
	//mostrar las sombras en las baldosas
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	
	private static final int anchoBorde = 5;// El ancho del borde alrededor del tablero de juego.
	 	
	/**
	 * El número de columnas del tablero.
	 */
	public static final int numCol = 10;
		
	/**
	 * El número de filas visibles en el tablero.
	 */
	private static final int numFilVisib  = 20;
	
	/**
	 * El número de filas que están ocultas a la vista.
	 */
	private static final int numFilOcultas = 2;
	
	/**
	 * El número total de filas que contiene el tablero.
	 */
	public static final int TotalFilas = numFilVisib + numFilOcultas;
	
	/**
	 * La cantidad de píxeles que ocupa un mosaico.
	 */
	public static final int tamFig = 24;
	
	/**
	 * El ancho del sombreado de las baldosas.
	 */
	public static final int anchSombra = 4;
	
	/**
	 * La coordenada x central en el tablero de juego.
	 */
	private static final int centroX = numCol * tamFig / 2;
	
	/**
	 * La coordenada y central en el tablero de juego.
	 */
	private static final int centroY = numFilVisib * tamFig / 2;
		
	/**
	 * El ancho total del panel.
	 */
	public static final int PANEL_ANCHO = numCol * tamFig + anchoBorde * 2;
	
	/**
	 * La altura total del panel.
	 */
	public static final int PANEL_ALTO = numFilVisib * tamFig + anchoBorde * 2;
	
	/**
	 * La fuente más grande para mostrar.
	 */
	private static final Font LETRA_GRANDE= new Font("Tahoma", Font.BOLD, 16);

	/**
	 * La fuente más pequeña para mostrar.
	 */
	private static final Font LETRA_PEQUENIA = new Font("Tahoma", Font.BOLD, 12);
	
	private Tetris tetris;
	
	/**
	 * Las fichas que componen el tablero.
	 */
	private Figura [][] fig;
		
	/**
	 * Crea una nueva instancia de GameBoard
	 */
	public Tablero(Tetris tetris) {
		this.tetris = tetris;
		this.fig = new Figura [TotalFilas][numCol];
		
		setPreferredSize(new Dimension(PANEL_ANCHO, PANEL_ALTO));
		setBackground(Color.BLACK);
	}
	
	/**
	 * Reinicia el tablero y borra las fichas.
	 */
	public void Limpiar() {
		/*
		 *Recorra cada índice de mosaico y establezca su valor
                                               *anular para limpiar el tablero.
		 */
		for(int i = 0; i < TotalFilas; i++) {
			for(int j = 0; j < numCol; j++) {
				fig[i][j] = null;
			}
		}
	}
	
	/**
	* Determina si se puede colocar o no una pieza en las coordenadas.
	 */
	public boolean esValida(Figura  tipo, int x, int y, int rotacion) {
				
		//Asegúrese de que la pieza esté en una columna válida.
		if(x < -tipo.getRecuadroIzq(rotacion) || x + tipo.getDimension() - tipo.getRecuadroDer(rotacion) >= numCol) {
			return false;
		}
		
		//Asegúrese de que la pieza esté en una fila válida.
		if(y < -tipo.getRecuadroSupe(rotacion) || y + tipo.getDimension() - tipo.getRecuadroInferior(rotacion) >= TotalFilas) {
			return false;
		}
		
		/*
		 * Recorra cada mosaico de la pieza y vea si entra en conflicto con uno existente.
                  * Nota: está bien hacer esto aunque permite el ajuste porque ya hemos
                  * Verificado para asegurarse de que la pieza se encuentra en una ubicación válida.
		 */
		for(int col = 0; col < tipo.getDimension(); col++) {
			for(int fila = 0; fila < tipo.getDimension(); fila++) {
				if(tipo.esFig(col, fila, rotacion) && ocupado(x + col, y + fila)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Agrega una pieza al tablero de juego. Nota: No comprueba si hay piezas existentes,
                       * y los sobrescribirá si existen  
	 */
	public void addPiesa(Figura  tipo, int x, int y, int rotacion) {
		/*
		 * Pase por cada mosaico dentro de la pieza y agréguelo
                 *  al tablero solo si el booleano que representa ese
                 * mosaico se establece en verdadero.
		 */
		for(int col = 0; col < tipo.getDimension(); col++) {
			for(int fila = 0; fila < tipo.getDimension(); fila++) {
				if(tipo.esFig(col, fila, rotacion)) {
					setFig(col + x, fila + y, tipo);
				}
			}
		}
	}
	
	/**
	* Comprueba el tablero para ver si se han limpiado las líneas, y
                       * los elimina del juego.
        */
	public int revLineas() {
		int lineCompletas = 0;
		
		/*
		 * Aquí recorremos cada línea y la comprobamos para ver si
                   * se ha borrado o no. Si es así, incrementamos el
                   * número de líneas completadas y marque la siguiente fila.
                        
                    *  La función checkLine se encarga de borrar la línea y
                    * Desplazar el resto del tablero hacia abajo para nosotros.
		 */
		for(int fila = 0; fila < TotalFilas; fila++) {
			if(revLinea(fila)) {
				lineCompletas++;
			}
		}
		return lineCompletas;
	}
			
	/**
	 * Comprueba si {@code fila} está llena o no.
	 */
	private boolean revLinea(int linea) {
		/*
		 * Repita todas las columnas de esta fila. Si alguno de ellos es
                 * vacío, la fila no está llena.
		 */
		for(int col = 0; col < numCol; col++) {
			if(!ocupado(col, linea)) {
			 return false;
			}
		}
		
		/*
		 * Dado que la línea está llena, necesitamos 'eliminarla' del juego.
                  * Para hacer esto, simplemente cambiamos cada fila por encima de ella hacia abajo en una.
		 */
		for(int fila = linea - 1; fila >= 0; fila--) {
			for(int col = 0; col < numCol; col++) {
				setFig(col, fila + 1, getFig(col, fila));
			}
		}
		return true;
	}
	
	
	/**
	 * Comprueba si el mosaico ya está ocupado.
	 */
	private boolean ocupado(int x, int y) {
		return fig[y][x] != null;
	}
	
	/**
	 * Establece un mosaico ubicado en la columna y fila deseadas.
	 */
	private void setFig(int  x, int y, Figura  tipo) {
		fig[y][x] = tipo;
	}
		
	/**
	 *Obtiene un mosaico por su columna y fila.
	 */
	private Figura  getFig(int x, int y) {
		return fig[y][x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Esto ayuda a simplificar el posicionamiento de las cosas.
		g.translate(anchoBorde, anchoBorde);
		
		/*
		 *Dibuja el tablero de manera diferente según el estado actual del juego.
		 */
		if(tetris.Pausado()) {
			g.setFont(LETRA_GRANDE);
			g.setColor(Color.WHITE);
			String msg = "PAUSA";
			g.drawString(msg, centroX - g.getFontMetrics().stringWidth(msg) / 2, centroY);
		} else if(tetris.NuevoJuego() || tetris.GameOver()) {
			g.setFont(LETRA_GRANDE);
			g.setColor(Color.WHITE);
			
			/*
			 * Debido a que tanto el juego terminado como las pantallas nuevas del juego son casi idénticas,
                                           * podemos manejarlos juntos y solo usar un operador ternario para cambiar
                                                      * los mensajes que se muestran 
			 */
			String msg = tetris.NuevoJuego() ? "TETRIS" : "GAME OVER";
			g.drawString(msg, centroX - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(LETRA_PEQUENIA);
			msg = "Presiona Enter para jugar" + (tetris.NuevoJuego() ? "" : "Otra vez");
			g.drawString(msg, centroX - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			
			/*
			 * Dibuja las fichas en el tablero.
			 */
			for(int x = 0; x < numCol; x++) {
				for(int y = numFilOcultas; y < TotalFilas; y++) {
					Figura  tile = getFig(x, y);
					if(tile != null) {
						dibFigura(tile, x * tamFig, (y - numFilOcultas) * tamFig, g);
					}
				}
			}
			
			/*
			 * Dibuja la pieza actual. Esto no se puede dibujar como el resto de los
                                                                     * piezas porque todavía no forma parte del tablero de juego. Si fuera
                                                                     * parte del tablero, sería necesario quitar cada marco que
                                                                      * sería lento y confuso. 
			  */
			 Figura  tipo = tetris.getFigAct();
			int piesaCol = tetris.getColFig();
			int piesaFila = tetris.getFilAct();
			int rotacion = tetris.getRotaFig();
			
			//Dibuja la pieza en el tablero.
			for(int col = 0; col < tipo.getDimension(); col++) {
				for(int fila = 0; fila < tipo.getDimension(); fila++) {
					if(piesaFila + fila >= 2 && tipo.esFig(col, fila, rotacion)) {
						dibFigura(tipo, (piesaCol + col) * tamFig, (piesaFila + fila - numFilOcultas) * tamFig, g);
					}
				}
			}
			
			/*
			 * Dibuja el fantasma (pieza semitransparente que muestra dónde aterrizará la pieza actual). No pude pensar en
                          * una mejor manera de implementar esto, por lo que tendrá que hacerlo por ahora. Simplemente tomamos la posición actual y nos movemos
                          * hacia abajo hasta que chocamos con una fila que provocaría una colisión.
			 */
			Color base = tipo.getColorBase();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int masBajo = piesaFila; masBajo < TotalFilas; masBajo++) {
				//Si no se detecta ninguna colisión, intente con la siguiente fila.
				if(esValida(tipo, piesaCol, masBajo, rotacion)) {					
					continue;
				}
				
				//Dibuja el fantasma una fila más arriba de la que tuvo lugar la colisión.
				masBajo--;
				
				//Dibuja la pieza fantasma.
				for(int col = 0; col < tipo.getDimension(); col++) {
					for(int fila = 0; fila < tipo.getDimension(); fila++) {
						if(masBajo + fila >= 2 && tipo.esFig(col, fila, rotacion)) {
				 			dibFigura(base, base.brighter(), base.darker(), (piesaCol + col) * tamFig, (masBajo + fila - numFilOcultas) * tamFig, g);
						}
					}
				}
				
		 		break;
			}
			
			/*
			 * Dibuja la cuadrícula de fondo sobre las piezas (sirve como un elemento visual útil
                         * para los jugadores, y hace que las piezas se vean mejor dividiéndolas. 
			 */
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < numCol; x++) {
				for(int y = 0; y < numFilVisib; y++) {
					g.drawLine(0, y * tamFig, numCol * tamFig, y * tamFig);
					g.drawLine(x * tamFig, 0, x * tamFig, numFilVisib * tamFig);
				}
			}
		}
		
		/*
		 * Dibuja el contorno.
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, tamFig * numCol, tamFig * numFilVisib);
	}
	
	/**
	 * Dibuja una figura en el tablero.
	 */
	private void dibFigura(Figura  tipo, int x, int y, Graphics g) {
		dibFigura(tipo.getColorBase(), tipo.getColorClaro(), tipo.getColorOscuro(), x, y, g);
	}
	
	/**
	 *Dibuja una figura en el tablero.
	 */
	private void dibFigura(Color base, Color claro, Color oscuro, int x, int y, Graphics g) {
		
		/* 
		 *Rellena todo el mosaico con el color base.
		 */
		g.setColor(base);
		g.fillRect(x, y, tamFig, tamFig);
		
		/*
		 * Rellena los bordes inferior y derecho del mosaico con el color de sombreado oscuro.
		 */
		g.setColor(oscuro);
		g.fillRect(x, y + tamFig - anchSombra, tamFig, anchSombra);
		g.fillRect(x + tamFig - anchSombra, y, anchSombra, tamFig);
		 
		/*
		 * Rellena los bordes superior e izquierdo con el sombreado claro. Dibujamos una sola línea
                    * para cada fila o columna en lugar de un rectángulo para que podamos dibujar un bonito
                               * Mirando en diagonal donde se encuentran las sombras claras y oscuras.
		 */
		g.setColor(claro);
		for(int i = 0; i < anchSombra; i++) {
			g.drawLine(x, y + i, x + tamFig - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + tamFig - i - 1);
		}
	}

}
