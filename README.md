# 📝 [Offlist](./Offlist)

Ce plugin permet de gérer une whitelist en mode offline.

| Commande | Arguments | Description                      |
|----------|-----------|----------------------------------|
| add      | \<pseudo> | Ajoute un joueur à la whitelist  |
| remove   | \<pseudo> | Enleve un joueur de la whitelist |

L'auto-complétion dépend des permissions du joueur et des joueurs déjà dans la whitelist.

# 📍 [WorldSpawn](./WorldSpawn)

Ce plugin permet d'avoir un `/spawn` spécifique à chaque monde.

| Commande | Arguments | Description                         |
|----------|-----------|-------------------------------------|
| spawn    | Aucun     | Téléporte au spawn du monde courant |
| lobby    | Aucun     | Téléporte au spawn du monde `lobby` |

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