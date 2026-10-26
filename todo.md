# Sprint 02

## QOUI

1- Afficher les methode qui utilisent l'annotation

- UrlMapping avec une attribut url
- Format affichage:
  /dept/new DeptController -> create
- Gestion des exception quand url fausse
- Quand l'url en question est /dept/new on affiche les methodes qui sont dedans (create dans cette exemple)
- Sinon, afficher une exception qu'il le connait pas et affichers tous les liens qu'il connait

# Sprint 03
## Cone
creation classe manana Url sy Method (GET na POST)
changement du clee du Map pour cette classe
overrider equals et hasCode pour faire clee unique et eviter doublon
mis a jour du fonction pour avoir le map avec cette clee et par rapport a la methode(get post)


# Bis
creation de fonction void avec un system.out.println
creer un instance de cette classe (instanciena fona aloha isaky ny miantso)
invoker le methode apres

# Sprint 04
- Deplacer le load des class dans le Listner (precedemment dans init):
  - Pour charger toutes les classes dès que la page charge

# Sprint 07
- Cree une formulaire
- Binding: formulaire envoye et transformet les donne en objet
- Donne vue to controller
- save(nom, age, ...): les attributs donnees un par un d'abord et apres on en cree un objet
  - Pour charger toutes les classes dès que la page charge

# Sprint 09
- Session
- Cree une nouvelle class de session, attribut: Map<String, Object>
- Dans resolveArgument, ajouter une bloc de verification si c'est cette class session
- Si oui, remplir la Map avec les attributs du HttpServlet session
- Stocker le Map des session dans le servletContext

# Sprint 10
- Authorisation
- Cree une nouvelle annotation
- Methode action anaty controller dev associer url
- Il dit qu'une method ne peut etre utilsier que par un utilisateur connecte

# Sprint 10 Bis
- On doit ajouter des roles specifique a chaque fonction, on peut etre admin, manager, ..., ou on peut seuelemnent etre connecte, 
