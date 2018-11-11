# sloWnet javanski programček za iskanje sopomenk

Osnova:
Fišer, Darja, 2015, 
  Semantic lexicon of Slovene sloWNet 3.1, Slovenian language resource repository CLARIN.SI.
  http://hdl.handle.net/11356/1026
  

## Kaj program dela? 
Program vhodno datoteko razširi z vsemi najdenimi sopomenkami. Izhodna datoteka se lahko nato uporabi za ugotavljanje sentimenta.

Vhodna datoteka je .txt s po eno besedo (oz. besedno zvezo) na vrstico.
Program za vsako vrstico poizkuša poiskati sopomenke ter jih vse doda v izhodno datoteko.
Izhodna datoteka je prav tako .txt datoteka (s pripono _expanded.txt). 
Izhodna datoteka ne vsebuje duplikatov, tiste besede ki nimajo sopomenke so pa samo prepisane.

## Uporaba
Program sloWnet.jar poganjamo tako, da se v ukazni vrstici premaknemo v direktorij java/sloWnet ter kličemo program z ukazom:

```
java -jar dist/sloWnet.jar [vhodnaDatoteka] [parameter]
```

vhodnaDatoteka: pot do vhodne .txt datoteke, ki vsebuje besede, za katere iščemo sopomenke. 
parameter: vrednsot true ali false, ki programu pove, ali naj sopomenke išče samo med ročno preverjenimi besedami (33.546 besed), ali med vsemi (71.803 besed). Parameter ni obvezen ter je privzeto nastavljen na false.


Primer uporabe:
```
java -jar dist/sloWnet.jar "test.txt" true
java -jar dist/sloWnet.jar "C:\Users\Uporabnik\Desktop\test.txt" false
```


Priložena je tudi .bat skripta za Windows okolje (windows_skripta.bat), s katero lahko program poganjamo enostavneje. 
