# 📝 [Offlist](./Offlist)

Ce plugin permet de gérer une whitelist en mode offline.

| Commande | Arguments | Description                      |
|----------|-----------|----------------------------------|
| add      | \<pseudo> | Ajoute un joueur à la whitelist  |
| remove   | \<pseudo> | Enleve un joueur de la whitelist |

L'auto-complétion dépend des permissions du joueur et des joueurs déjà dans la whitelist.

# 📍 [PolyUtils](./PolyUtils)

Ce plugin ajoute des commandes utilitaires.

| Commande   | Arguments         | Description                                  |
|------------|-------------------|----------------------------------------------|
| spawn      | Aucun             | Téléporte au spawn du monde courant          |
| lobby      | Aucun             | Téléporte au spawn du monde `lobby`          |
| rezo join  | \<ville>          | Rejoindre le groupe de sa ville              |
| rezo give  | \<ville> <points> | Donner des points à une ville                |
| rezo reset | Aucun             | Réinitialiser les points des villes          |
| rezo sync  | Aucun             | Synchroniser les teams et la base de données |

# ⚽ [PolyEvents](./PolyEvents)

Ce plugin permet de créer des évènements (WIP).

| Commande | Arguments | Description                                                       |
|----------|-----------|-------------------------------------------------------------------|
| create   | \<nom>    | Crée un évènement à la position du joueur                         |
| delete   | \<nom>    | Supprime un évènement                                             |
| list     | Aucun     | Affiche une liste de tous les évènement disponibles               |
| join     | \<nom>    | Téléporte aux coordonnées de l'évènement                          |
| leave    | \<nom>    | Fait revenir le joueur où il était avant de rejoindre l'évènement |

L'auto-complétion dépend des permissions du joueur, des évènements disponibles et de ceux qu'il a déjà rejoint.