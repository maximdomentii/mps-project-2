# Master Minds
Van'Gogu

<pre>
Cimpian Vlad - Andrei   342C5   vladcimpian@gmail.com
Domentii Maxim          342C5   domentiimaxim@yahoo.com
Nedeloiu Adrian George  342C5   nedeloiuadrian@gmail.com
Tudureanu Romeo         342C5   romeo.tudureanu@gmail.com
</pre>

# Implementare
Proiectul nostru este realizat în JavaScript și HTML5 și constituie o imagine creată într-un tag canvas. La fiecare rulare (încărcare a paginii HTML) pe ecran este randata în timp real imaginea  (stilizata) a unui arbore de la tulpină până la frunze. 

La fiecare interval de timp prestabilit de noi, pe ecran se desenează un singur nivel al arborelui. Modelul din spate este un arbore binar, prin urmare, din fiecare branch vor porni alte două branch-uri. 

Datorită unor parametrii aleși random la fiecare rulare, forma și dimensiunea arborilor nu vor fi "niciodată" aceleași. 

Printre acești parametrii se numără: unghiul format de un branch copil branch-ului tată, procentul de reducere în grosime și lungime a ramurii copil față de ramura părinte, gradul de înclinare al subarborilor. 

Un parametru aleator este și culoarea ramurilor care este aleasa la fiecare nivel, deci toate ramurile de pe același nivel vor avea aceeași culoare. Am ales această metodă pentru un aspect mai frumos al imaginii.

# Rulare
Pentru a rula proiectul trebuie descarcate cele doua fisiere (main.html si fisierul .js cu libraria jQuery) in acelasi director.
Se deschide fisierul main.html intr-un browser iar pentru a genera o imagine noua se face refresh la pagina.

