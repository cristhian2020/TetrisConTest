 

public class Reloj {
	
	/**
	 * La cantidad de milisegundos que componen un ciclo.
	 */
	private float millisPerCycle;
	
	/**
	 * La última vez que se actualizó el reloj (utilizado para calcular el
                        * tiempo delta).
	 */
	private long lastUpdate;
	
	/**
	 * El número de ciclos que han transcurrido y aún no se han sondeado.
	 */
	private int elapsedCycles;
	
	/**
	 * La cantidad de tiempo en exceso hacia el siguiente ciclo transcurrido.
	 */
	private float excessCycles;
	
	/**
	 * Si el reloj está en pausa o no.
	 */
	private boolean isPaused;
	
	/**
	 * Crea un nuevo reloj y establece sus ciclos por segundo.
	 */
	public Reloj(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	/**
	 * Establece el número de ciclos que transcurren por segundo.
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	/**
	 * Restablece las estadísticas del reloj. Los ciclos transcurridos y el exceso de ciclo se restablecerán
                       * a 0, la última hora de actualización se restablecerá a la hora actual y
                       * el indicador de pausa se establecerá en falso.
	 */
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	/**
	 *Actualiza las estadísticas del reloj. El número de ciclos transcurridos, así como el
                     * El exceso de ciclo se calculará solo si el reloj no está en pausa. Esta
                     * Se debe llamar al método en cada fotograma incluso cuando está en pausa para evitar
                     * desagradables sorpresas con el tiempo delta.
	 */
	public void update() {
		//Obtenga la hora actual y calcule el tiempo delta.
		long currUpdate = getCurrentTime();
		float delta = (float)(currUpdate - lastUpdate) + excessCycles;
		
		//Actualice la cantidad de tics transcurridos y en exceso si no estamos en pausa.
		if(!isPaused) {
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		
		//Configure la última hora de actualización para el próximo ciclo de actualización.
		this.lastUpdate = currUpdate;
	}
	
	/**
	 * Pausa o reactiva el reloj. Mientras está en pausa, un reloj no se actualizará
                       * ciclos transcurridos o exceso de ciclos, aunque el método {@code update} debería
                        * Todavía se llamará a cada fotograma para evitar problemas.
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	/**
	 * Comprueba si el reloj está actualmente en pausa.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Comprueba si ya ha transcurrido un ciclo para este reloj. Si es así,
                       * el número de ciclos transcurridos se reducirá en uno.	
	 */
	public boolean hasElapsedCycle() {
		if(elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}
	
	/**
	 * Comprueba si ya ha transcurrido un ciclo para este reloj. diferente a
                       * {@code hasElapsedCycle}, la cantidad de ciclos no disminuirá
                      * si el número de ciclos transcurridos es mayor que 0.
	 */
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}
	
	/**
	 * Calcula la hora actual en milisegundos usando el alto de la computadora
                        * reloj de resolución. Esto es mucho más confiable que
	 */
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}

