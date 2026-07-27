# خدماتي (Khadamate)

Application Android pour mettre en relation clients et artisans au Maroc (plombier, mécanicien, électricien, menuisier).

## Fonctionnalités

- Inscription client / artisan
- Connexion Firebase Auth
- Liste des artisans par métier, triés par distance GPS
- Profil artisan avec appel, itinéraire, likes/dislikes
- Tableau de bord artisan (répartition des avis)
- Favoris (artisans aimés)

## Améliorations v1.1

- Correction des validations d’inscription (client et artisan)
- Comptes liés à l’UID Firebase Auth
- Tri des artisans du plus proche au plus loin
- Réinitialisation du mot de passe
- Boutons appel / cartes sur le profil
- États vides, gestion d’erreurs, listeners Firebase nettoyés
- Localisation moins agressive (batterie)
- Identité visuelle teal/ambre et layouts plus responsives

## Stack

- Java, Support Library 28
- Firebase Auth + Realtime Database
- MPAndroidChart

## Build

```bash
./gradlew assembleDebug
```

> Note : le toolchain actuel (AGP 3.2 / `targetSdk 28`) est historique. Une migration AndroidX + `targetSdk` récent est recommandée pour la publication Play Store.
