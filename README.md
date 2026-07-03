# Framework

Framework Java créé from scratch pour gérer un **front controller** basé sur des annotations, avec routage des requêtes HTTP vers des méthodes de contrôleurs.

## Présentation

Ce projet est un framework web léger en Java, pensé pour faciliter la création d’applications web en centralisant le traitement des requêtes via un seul contrôleur frontal.

Il permet notamment :
- de déclarer des contrôleurs avec une annotation dédiée ;
- de mapper des URL vers des méthodes ;
- de gérer des méthodes HTTP comme `GET` et `POST` ;
- d’instancier automatiquement les contrôleurs et d’invoquer la méthode cible ;
- d’afficher les routes connues lorsqu’une URL est inconnue.

## Fonctionnalités

- **Annotation `@Controller`** pour marquer les classes contrôleurs.
- **Annotation `@UrlMapping`** pour associer une méthode à une URL.
- Support des méthodes HTTP :
  - `GET`
  - `POST`
- **Front controller** basé sur `HttpServlet`.
- Chargement automatique des classes de contrôleurs.
- Gestion des erreurs pour les routes inconnues.
- Détection des routes dupliquées via une exception dédiée.

## Structure du projet

```text
src/
└── mg/itu/framework/
    ├── annotation/
    │   ├── Controller.java
    │   └── UrlMapping.java
    ├── controller/
    │   └── FrontServletController.java
    ├── dto/
    │   ├── MethodDTO.java
    │   └── RequestMapping.java
    ├── exception/
    │   └── RouteDejaDefinieException.java
    └── utils/
        └── Utilitaire.java
lib/
└── servlet-api.jar
run.sh
```

## Principe de fonctionnement

1. Une classe est annotée avec `@Controller`.
2. Ses méthodes sont annotées avec `@UrlMapping(url = "...", method = "...")`.
3. Le `FrontServletController` charge les classes du package configuré.
4. Lorsqu’une requête HTTP arrive :
   - le framework récupère l’URL et la méthode HTTP ;
   - il cherche le mapping correspondant ;
   - si le mapping existe, la méthode est invoquée ;
   - sinon, une page HTML affiche les routes connues.

## Exemple d’utilisation

### Contrôleur

```java
import mg.itu.framework.annotation.Controller;
import mg.itu.framework.annotation.UrlMapping;

@Controller
public class DeptController {

    @UrlMapping(url = "/dept/new", method = "GET")
    public void create() {
        System.out.println("Création d’un département");
    }
}
```

### Déclaration dans le servlet

Le framework repose sur un servlet frontal abstrait :

```java
public class MyServlet extends FrontServletController {
}
```

Puis dans la configuration web, il faut définir le paramètre d’init `packages` avec les packages à scanner.

Exemple :
```xml
<init-param>
    <param-name>packages</param-name>
    <param-value>mon.package.controllers</param-value>
</init-param>
```

Si plusieurs packages doivent être chargés, ils peuvent être séparés par `;;`.

Exemple :
```xml
<param-value>mon.package.controllers;;autre.package.controllers</param-value>
```

## Compilation et génération du JAR

Le projet fournit un script `run.sh` qui :

- nettoie le dossier de compilation ;
- compile les fichiers Java ;
- génère `framework.jar`.

```bash
./run.sh
```

## Dépendances

- Java
- Servlet API
- Apache Tomcat

Le script de compilation utilise :
- `lib/servlet-api.jar`
- un Tomcat installé localement

## Gestion des erreurs

Le framework signale :
- les URL inconnues ;
- les routes dupliquées ;
- les erreurs d’invocation réflexive.

## Remarques

Ce projet est en cours d’évolution et sert de base à un framework Java maison avec annotation, mapping et front controller.

## Auteur

Projet développé par **5teeve**.
