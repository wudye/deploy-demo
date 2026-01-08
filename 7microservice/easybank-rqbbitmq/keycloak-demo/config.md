Configure the demo-client in Keycloak so that it works with your Spring Boot application,
follow these steps in the Keycloak Admin Console:
#   Log in to Keycloak Admin Console•
    Go to your Keycloak URL (e.g., http://192.168.80.129:7080/) and log in as an admin. Create a new realm
    •Select the demo-realm from the top-left dropdown menu.
# Create or Edit the Client
    Click on Clients in the left menu.
    •If demo-client does not exist, click Create client.
    •General Settings:
        •Client type: OpenID Connect
        •Client ID: demo-client
        •Name: Demo Client (optional)•Click Next.
# Capability Config
    Client authentication: On (This is crucial. It corresponds to confidential access type. It enables the Client Secret).
    Authorization: Off (unless you need fine-grained authorization services).
    Authentication flow:
        Standard flow: Checked (This is required for authorization_code login via browser).
        Direct access grants: Checked (Optional, but useful if you want to test with curl using username/password directly as mentioned in your note.md).
        Implicit flow: Unchecked.
        Service accounts roles: Checked (optional, often used for machine-to-machine).

# Access Settings (Important)
    Root URL: http://localhost:8080/
    Home URL: http://localhost:8080/home
    Valid redirect URIs:
        http://localhost:8080/login/oauth2/code/keycloak
        http://localhost:8080/login?logout (for the logout redirect)
        http://localhost:8080/* (Optional, for easier development)
    Valid post logout redirect URIs (if available in your Keycloak version):
        http://localhost:8080/login?logout
        http://localhost:8080/*

    Web origins: http://localhost:8080 (or + to allow all valid redirect URIs).•Click Save.
#  Credentials (Client Secret)
    Go to the Credentials tab of the demo-client.You will see a Client Secret.
    Action Required:
        Copy this secret.Update the client-secret value to match what is in Keycloak.

# Create the User (if not exists)


look for a field named Required User Actions. You will likely see an item in the box called Update Password.
.Click the "X" next to Update Password (and any other actions listed there) to remove it. The box should become empty.