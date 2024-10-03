#!/bin/bash

# Define the file paths
input_file='ulyss12-sharded-tmp.txt'
output_file='ulyss12-sharded.txt'

# Define the chunk size (100000 words)
CHUNK_SIZE=100000

# Step 1: Read the input file and split content into words
words=($(cat "$input_file"))

# Initialize variables
result=""
word_count=0
current_chunk=""

# Step 2: Iterate through words and chunk them
for word in "${words[@]}"; do
    current_chunk+="$word "

    # Increase word count
    word_count=$((word_count + 1))

    # If the chunk size is reached, append to the result
    if [[ $word_count -eq $CHUNK_SIZE ]]; then
        result+="${current_chunk% }"$'\r\n'
        current_chunk=""
        word_count=0
    fi
done

# Append any remaining words in the last chunk
if [[ -n $current_chunk ]]; then
    result+="${current_chunk% }"
fi

# Step 3: Write the result to the output file
echo -e "$result" > "$output_file"
