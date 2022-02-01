 

import java.awt.Color;
public enum Figura {
/**
	 * FigI
	 */
	 FigI(new Color(Tablero.COLOR_MIN,Tablero.COLOR_MAX, Tablero.COLOR_MAX), 4, 4, 1, new boolean[][] {
	{
		false,	false,	false,	false,
		true,	true,	true,	true, //dibuja la I en orisontal 
		false,	false,	false,	false,
		false,	false,	false,	false,
	},
	{
		false,	false,	true,	false,
		false,	false,	true,	false, //dibuja la I en vertica
		false,	false,	true,	false,
		false,	false,	true,	false, 
	},
	{
		false,	false,	false,	false,
		false,	false,	false,	false,
		true,	true,	true,	true, //dibuja la I en orisontal 
		false,	false,	false,	false,
	},
	{
		false,	true,	false,	false,
		false,	true,	false,	false,//dibuja la I en vertica
		false,	true,	false,	false,
		false,	true,	false,	false,
	}
 }),
	
/**
 * FigJ
 */

FigJ(new Color(Tablero.COLOR_MIN, Tablero.COLOR_MIN, Tablero.COLOR_MAX), 3, 3, 2, new boolean[][] {
	{
		true,	false,	false,
		true,	true,	true,
		false,	false,	false,
	},
	{
		false,	true,	true,
		false,	true,	false,
		false,	true,	false,
	},
	{
		false,	false,	false,
		true,	true,	true,
		false,	false,	true,
	},
	{
		false,	true,	false,
		false,	true,	false,
		true,	true,	false,
	}
 }),
	
/**
 * FigL
 */
 FigL(new Color(Tablero.COLOR_MAX, 127, Tablero.COLOR_MIN), 3, 3, 2, new boolean[][] {
	{
		false,	false,	true,
		true,	true,	true,
		false,	false,	false,
	},
	{
		false,	true,	false,
		false,	true,	false,
		false,	true,	true,
	},
	{
		false,	false,	false,
		true,	true,	true,
		true,	false,	false,
	},
	{
		true,	true,	false,
		false,	true,	false,
		false,	true,	false,
	}
}),
	
/**
 * Piece TypeO.
 */
FigO(new Color(Tablero.COLOR_MAX,Tablero.COLOR_MAX, Tablero.COLOR_MIN), 2, 2, 2, new boolean[][] {
	{
		true,	true,
		true,	true,
	},
	{
		true,	true,
		true,	true,
	},
	{	
		true,	true,
		true,	true,
	},
	{
		true,	true,
		true,	true,
	}
}), 
	
/**
 * Piece TipoS.
 */
FigS(new Color( Tablero.COLOR_MIN,Tablero.COLOR_MAX, Tablero.COLOR_MIN), 3, 3, 2, new boolean[][] {
	{
		false,	true,	true,
		true,	true,	false,
		false,	false,	false,
	},
	{
		false,	true,	false,
		false,	true,	true,
		false,	false,	true,
	},
	{
		false,	false,	false,
		false,	true,	true,
		true,	true,	false,
	},
	{
		true,	false,	false,
		true,	true,	false,
		false,	true,	false,
	}
 }),
	
/**
 * Fig TipoT.
 */
   FigT(new Color(128, Tablero.COLOR_MIN, 128), 3, 3, 2, new boolean[][] {
	{
		false,	true,	false,
		true,	true,	true,
		false,	false,	false,
	},
	{
		false,	true,	false,
		false,	true,	true,
		false,	true,	false,
	},
	{
		false,	false,	false,
		true,	true,	true,
		false,	true,	false,
	},
	{
		false,	true,	false,
		true,	true,	false,
		false,	true,	false,
	}
  }),
	
  /**
  * Fig TipoZ.
  */
  FigZ(new Color(Tablero.COLOR_MAX, Tablero.COLOR_MIN,Tablero.COLOR_MIN), 3, 3, 2, new boolean[][] {
	{
		true,	true,	false,
		false,	true,	true,
		false,	false,	false,
	},
	{
		false,	false,	true,
		false,	true,	true,
		false,	true,	false,
	},
	{
		false,	false,	false,
		true,	true,	false,
		false,	true,	true,
	},
	{
		false,	true,	false,
		true,	true,	false,
		true,	false,	false,
	}
  });
		
	private Color ColorBase;
	
	private Color ColorClaro;
	
	private Color ColorOscuro;
	
	private int apareserCol;
	
	private int apareserFil;
	
	/**
	 * Las dimensiones de la matriz de esta pieza.
	 */
	private int dimension;
	
	private int filas;
	
	private int cols;
	
	/**
	 * Los azulejos de esta pieza. Cada pieza tiene una serie de fichas para cada rotación.
	 */
	private boolean[][] Figuras;
	
	
	private Figura (Color color, int dimension, int cols, int filas, boolean[][] Figuras) {
		this.ColorBase = color;
		this.ColorClaro = color.brighter();
		this.ColorOscuro = color.darker();
		this.dimension = dimension;
		this.Figuras = Figuras;
		this.cols = cols;
		this.filas= filas;
		
		this.apareserCol = 5 - (dimension >> 1);
		this.apareserFil = getRecuadroSupe(0);
	}
	
	/**
	 * Obtiene el color base de este tipo.
	 */
	public Color getColorBase() {
		return ColorBase;
	}
	
	/**
	 * Obtiene el color de sombreado claro de este tipo
	 */
	public Color getColorClaro() {
		return ColorClaro;
	}
	
	/**
	 * Obtiene el color de sombreado oscuro de este tipo.	
	 */
	public Color getColorOscuro() {
		return ColorOscuro;
	}
	
	/**
	 * Obtiene la dimensión de este tipo.
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Obtiene la columna de generación de este tipo.
	 */
	public int getApareserColumn() {
		return apareserCol;
	}
	
	/**
	 * Obtiene la fila de generación de este tipo.
	 */
	public int getApareserFil() {
		return apareserFil;
	}
	
         /*
	  * Obtiene el número de filas de esta pieza. (Solo válido cuando la rotación es 0 o 2,
                      * pero está bien ya que esto solo se usa para la vista previa que usa la rotación 0).
	 */
	 public int getFilas() {
		return filas;
	 }
	 
	/**
	 Obtiene el número de columnas de esta pieza. (Solo válido cuando la rotación es 0 o 2,
         * pero está bien ya que esto solo se usa para la vista previa que usa la rotación 0).
	 */
	 public int getCols() {
		return cols;
	}
	
	/**
	 * Comprueba si las coordenadas y la rotación dadas contienen un mosaico.	 
	 */
	public boolean esFig(int x, int y, int rotacion) {
		return Figuras[rotacion][y * dimension + x];
	}
	
	/**
	 * El recuadro izquierdo está representado por el número de columnas vacías de la izquierda
                        * lado de la matriz para la rotación dada.
	 */
	 public int getRecuadroIzq(int rotacion) {
		/*
		 *Recorre de izquierda a derecha hasta encontrar un mosaico y luego regresa
                  * la columna.                  
		 */
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(esFig(x, y, rotacion)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * El recuadro derecho está representado por el número de columnas vacías de la izquierda
         * lado de la matriz para la rotación dada.
	 */
	 public int getRecuadroDer(int rotacion) {
		/*
		 * Recorra de derecha a izquierda hasta que encontremos un mosaico y luego regrese
                 * la columna.
		 */
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(esFig(x, y, rotacion)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	
	/**
	* El recuadro izquierdo está representado por el número de filas vacías en la parte superior
         * lado de la matriz para la rotación dada.
	 */
	public int getRecuadroSupe(int rotacion) {
		/*
		 * Recorra de arriba a abajo hasta que encontremos un mosaico y luego regrese
                                              * La fila.
		 */
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(esFig(x, y, rotacion)) {
					return y;
				}
			}
		}
		return -1;
	}
	
	/*
	  *El recuadro inferior está representado por el número de filas vacías en la parte inferior
          * lado de la matriz para la rotación dada.
	 */
	public int getRecuadroInferior(int rotacion) {
		/*
		 * Recorre de abajo hacia arriba hasta que encontremos un mosaico y luego regresemos
                 * La fila.
		 */
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(esFig(x, y, rotacion)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
	
}
