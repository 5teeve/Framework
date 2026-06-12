# Framework
## Contexte
- Cree une framework avec Java (servlet)
## Resultat a attendre
- Une framework en format jar importable dans une application
## Fonctionnalite:
- Afficher une message de bienvenue dans tous les liens qui utilsent le framework

# COMMENT
## Stack
- Java 17+
- Servlet (jakarta.servlet-api)
- Tomcat 10+

## Structure du projet
```
framework/
├── lib/
│   └── jakarta.servlet-api-6.0.0.jar
├── src/
│   └── com/
│       └── framework/
│           └── BaseServlet.java
└── out/
```

## Etapes

### 1 — BaseServlet.java
- Creer la classe `BaseServlet` dans le package `com.framework`
- Elle `extends HttpServlet`
- Override `doGet()` et `doPost()`
- Dans ces methodes : ecrire le HTML de bienvenue dans `response.getWriter()`
- La classe est `abstract` — elle ne s'utilise pas directe, on l'etend

### 2 — Compiler
```
javac -cp lib/servlet-api.jar -d out src/com/framework/BaseServlet.java
```

### 3 — Generer le JAR
```
jar cf framework.jar -C out .
```
- Le jar se trouve a la racine du projet

## Utilisation dans une autre application
- Creer un projet web avec cette structure :
```
mon-app/
└── WEB-INF/
    ├── lib/
    │   └── framework.jar
    ├── classes/
    │   └── com/demo/HomeServlet.class
    └── web.xml
```
- Creer une servlet qui fait `extends BaseServlet`
- Annoter avec `@WebServlet("/ma-route")` ou declarer dans `web.xml`
- Copier le dossier `mon-app/` dans `webapps/` de Tomcat
- Lancer Tomcat et aller sur `http://localhost:8080/mon-app/ma-route`
- Aucun code supplementaire — le message s'affiche automatiquement
