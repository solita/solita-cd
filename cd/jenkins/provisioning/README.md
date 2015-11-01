# cd/jenkins/provisioning

Tämä osa asentaa Jenkins sovelluksen, plugineita ja Jenkinsin tarvitsemat muut osat. 

Asennus tapahtuu hallintakoneelta kohdekoneelle. Kohdekoneen nimi tai ip-osoite tulee kirjoittaa
environment/prod/inventory tiedostoon.

Asennus olettaa, että kohdekoneelle on suora pääsy hallintakoneelta ssh-avaimien avulla. Tämän varmistamiseksi on
hallintakoneella ensin luotava ssh-avain ja se pitää asentaa kohdekoneelle.

Ssh-avain luodaan komennolla `ssh-keygen` ja se kopioidaan kohdekoneelle komennolla `ssh-copy-id root@kohdekone`

Toistaiseksi käytämme oletuksena root-tunnusta, haluttaessa voidaan käyttää myös muuta tunnusta, 
jolla on sudo-oikeudet.

Jenkinsiin asennetaan halutut pluginit, jotka on määritelty `global_vars/main.yml`:ssa.

Asennus tapahtuu komennolla `ansible-playbook -i environment/prod/inventory site.yml`

Asennus kestää muutaman minuutin riippuen kohdekoneen verkkoyhteyden nopeudesta sen noutaessa asennuspaketteja
internetistä.
