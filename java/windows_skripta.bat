@echo off

SET /P file="Pot do vhodne datoteke (za test vnesi "test.txt"): "
SET /P par="Uporabim samo rocno preverjene besede? (true/false): "

CALL :NORMALIZEPATH %file%
SET absFile=%RETVAL%

cd sloWnet
java -jar "dist/sloWnet.jar" %absFile% %par%

pause

:: ========== FUNCTIONS ==========
EXIT /B

:NORMALIZEPATH
  SET RETVAL=%~dpfn1
  EXIT /B