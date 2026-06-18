- Pre-requis
Annotation
Chargement classe
Demarrage appli-web(opt):ContextListener
  - Parcours dans classpath par package ou all
  - Annotation mg.itu.xxx.annotation.Controller
  - Sprint01
  Objectifs:
  Verifier si une classe est annotee par Controller et ajouter dans un list
  afficher les classes annotees dans processrequest de FrontController
 - FrontController:
    List<String> listecontroller
    initParam dans web.xml se trouve le package de controller configure par l utilisateur
    appeler la fonction utilitaire dans init
 - Utilitaire: 
  Creer une classe utilitaire avec des methodes :
   chargerClassePath(package name);
   findController(List<?> classes)
   
