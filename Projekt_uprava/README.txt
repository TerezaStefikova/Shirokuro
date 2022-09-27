Tereza Štefíková - Shirokuro

Je to japonská hra, v ktorej spájate biele a čierne body.
Hra má 10 levelov, pričom sa postupne zvyšuje obtiažnosť aj veľkosť hracej plochy.
1. level je skúšobný, len aby ste si vyskúšali spájanie. 
(Info k levelom pozr. prilozeny súbor: levely_zo_stranky.txt)

Pravidlá:
	o Nemôžete spojiť 2 body rovnakej farby.
	o Môžete spájať len navzájom susedné body, či už po osi X-ovej alebo osi Y-ovej -teda 
	  nemôžete spájať body digonálne.
	o Nemôžete spojiť 2 body ak by sa ich spojenie križovalo s už vytvoreným spojením.

Funkcionalita:
	o Hra sa ovláda kliknutiami na bod. 
	  Najprv si označíte jeden bod (zostane svietiť na oranžovo), nasledujúcim kliknutím
	  si vyberiete druhý bod a, ak tento 2. bod spolu s prývm spĺňa vyššie uvedené podmienky,
	  vytvorí sa spojenie (oba body zostanú svietiť na oranžovo a nakreslí sa medzi nimi 
	  oranžová úsečka).
	o Ak sa rozhodnete zrušiť nejaké spojenie, stačí kliknúť na jeden z jeho 2 bodov.
	o Ak máte označený prvý bod, kľudne môžete pred výberom druhého bodu rušiť spojenia, 
	  bez toho, že by sa vám vaše označenie zrušilo.
	o Hra vám nedovolí vytvoriť dvojicu, ktorá by nespĺňala jej pravidlá.
	o Keď dokončíte level, kliknutím na tlačidlo "next" sa dostanete do ďaľšieho levelu.
	o Hra vás nepustí ďalej, ak nemáte hotový level - na 2 sekundy vypíše upozornenie.
	o Až dokončíte posledný level, po kliknutí na tlačidlo next vám vypíše gratuláciu a tlačidlo
	  "next" sa zmení na "quit", po jeho kliknutí aplikáciu zavriete. 


