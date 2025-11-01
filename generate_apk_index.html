#!/bin/bash

# A script to generate an index.html file with links to all .apk files
# in the current directory.

# --- Configuration ---
OUTPUT_FILE="index.html"
TITLE="APK File Index"
HEADING="Available APK Files"

# --- Safety & Setup ---
# Exit immediately if a command exits with a non-zero status.
set -e

# Set shell option so that if no .apk files are found, the loop
# doesn't run with a literal "*.apk" as the filename.
shopt -s nullglob

# --- Main Logic ---

# 1. Create the header of the HTML file using a "here document".
#    This overwrites any existing index.html file.
cat <<EOF > "$OUTPUT_FILE"
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>$TITLE</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            line-height: 1.6;
            background-color: #f4f4f9;
            color: #333;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px 40px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #0056b3;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            margin-bottom: 12px;
        }
        a {
            display: block;
            text-decoration: none;
            color: #007BFF;
            background-color: #e9ecef;
            padding: 12px 15px;
            border-radius: 5px;
            border-left: 4px solid #007BFF;
            transition: background-color 0.2s ease-in-out, transform 0.1s ease;
        }
        a:hover {
            background-color: #d1ecf1;
            transform: translateX(5px);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>$HEADING</h1>
        <ul>
EOF

# 2. Loop through all .apk files in the current directory.
#    The glob '*.apk' will expand to a list of matching files.
file_count=0
for apk_file in *.apk; do
    # For each file, append a list item with a link to the HTML file.
    # We use printf for safer formatting, especially with filenames that might contain special characters.
    # The link text and the href are both the filename.
    printf '            <li><a href="%s">%s</a></li>\n' "$apk_file" "$apk_file" >> "$OUTPUT_FILE"
    ((file_count++))
done

# 3. Create the footer of the HTML file and append it.
cat <<EOF >> "$OUTPUT_FILE"
        </ul>
        <hr style="margin-top: 30px;">
        <p style="text-align: center; color: #777; font-size: 0.9em;">
            Generated on $(date)
        </p>
    </div>
</body>
</html>
EOF

# --- Completion Message ---
if [ "$file_count" -eq 0 ]; then
    echo "No .apk files found in the current directory. An empty index.html was created."
else
    echo "Successfully generated '$OUTPUT_FILE' with $file_count file links."
fi

