# Analiza sentimenta Rtvslo novic

Sistem za analizo sentimenta Rtvslo novic in komentarjev, ki se nahajajo pod izbranim iskalnim nizom.

## Zgradba

Zraven sta priložena program za [lematizacijo](http://oznacevalnik.slovenscina.eu/Vsebine/Sl/ProgramskaOprema/Navodila.aspx) 
(mapa 'ObeliksTagger) in program za iskanje sopomenk [SloWNet](https://www.clarin.si/repository/xmlui/handle/11356/1026) (mapa 'java').

V mapi 'webdriver' se nahaja Chrome webdriver za parsanje novic, v mapi 'notebooks' pa se nahaja glavni del sistema, 
ki je napisan v Jupyter Python notebookih.

Za analizo sentimenta sva zgradila slovar pozitivnih in negativnih besed. Nahajajo se v ObeliksTagger/Sentiment

## Uporaba

V mapi 'notebooks' sta pomembni dve datoteki: 'Parser final' in 'Analiza'.

'Parser final' vsebuje parser za parsanje novic, naslovov in komentarjev glede na podani iskalni niz. Rezultate zapiše v datoteko.

'Analiza' vsebuje metode za branje datotek z novicami, lematizacijo (uporaba .exe porgrama je implementirana v notebooku) in 
splošno analizo: clustering ter sentiment.

Bloke kode poganjamo po vrsti.

## Opombe

V mapi 'notebooks' se nahajta dva notebooka z naslovom Analiza - poročilo... . 
Datoteki vsebujeta rezultate uporabljene v poročilu. 
