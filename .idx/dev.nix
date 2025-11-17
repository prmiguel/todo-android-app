# To learn more about how to use Nix to configure your environment
# see: https://firebase.google.com/docs/studio/customize-workspace
{ pkgs, ... }: {
  # Which nixpkgs channel to use.
  channel = "stable-24.05"; # or "unstable"

  # Use https://search.nixos.org/packages to find packages
  packages = [
    pkgs.jdk17
    pkgs.android-sdk
  ];

  # Sets environment variables in the workspace
  env = {
    JAVA_HOME = "${pkgs.jdk17}";
    ANDROID_HOME = "${pkgs.android-sdk}";
  };
  idx = {
    # Search for the extensions you want on https://open-vsx.org/ and use "publisher.id"
    extensions = [
      "google.android-ide"
      "redhat.java"
      "vscjava.vscode-java-debug"
      "vscjava.vscode-java-test"
      "vscjava.vscode-maven"
    ];

    # Enable previews
    previews = {
      enable = true;
      previews = {
        android = {
          manager = "android";
          # To use a different API level, see https://search.nixos.org/packages?channel=unstable&query=android-emulator-images
          # and replace the package below.
          package = pkgs.android-emulator-images.system-images_x86-64_34;
        };
      };
    };

    # Workspace lifecycle hooks
    workspace = {
      # Runs when a workspace is first created
      onCreate = {
        # Example: install JS dependencies from NPM
        # npm-install = "npm install";
      };
      # Runs when the workspace is (re)started
      onStart = {
        # Example: start a background task to watch and re-build backend code
        # watch-backend = "npm run watch-backend";
      };
    };
  };
}
