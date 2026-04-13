# Hashiwokakero-Bridges
🧩 Hashiwokakero Solver (Java + Z3 SMT) 

Questo progetto implementa una soluzione al puzzle logico Hashiwokakero utilizzando Java e un solver SMT (Z3).

⸻

🔹 Descrizione del problema

Hashiwokakero è un puzzle su griglia in cui alcune celle contengono isole con un numero (grado).

L’obiettivo è collegare tutte le isole con ponti rispettando i seguenti vincoli:

	•	ogni isola deve avere un numero di ponti pari al proprio grado
  
	•	i ponti possono essere solo orizzontali o verticali
  
	•	i ponti non possono incrociarsi
  
	•	tutte le isole devono essere connesse in un unico componente

⸻

🔹 Approccio implementativo

Il problema è stato modellato come un problema di Satisfiability Modulo Theories (SMT) e risolto tramite il solver Z3.

⸻

🧠 Modellazione

	•	Ogni possibile ponte tra due isole è rappresentato come una variabile (0, 1 o 2 ponti)
  
	•	Le isole sono identificate da coordinate (row, col) e un ID univoco
  
	•	I vincoli del puzzle vengono tradotti in vincoli logico-aritmetici

⸻

📌 Vincoli principali

	•	Vincolo di grado: la somma dei ponti incidenti a ogni isola deve rispettare il valore assegnato
  
	•	Vincolo di non incrocio tra ponti
  
	•	Vincolo di connettività globale delle isole

⸻

🔹 Complessità

	•	Riduzione a SMT: tempo polinomiale rispetto al numero di isole e ponti possibili
  
	•	Risoluzione: esponenziale nel caso peggiore (problema NP-completo)
