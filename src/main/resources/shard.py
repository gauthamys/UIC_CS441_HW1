# Define the file paths
input_file = 'ulyss12-sharded-tmp.txt'
output_file = 'ulyss12-sharded.txt'

# Define the chunk size (100000 words)
CHUNK_SIZE = 100000

# Step 1: Read the input file
with open(input_file, 'r') as f:
    content = f.read()

# Step 2: Split the content into words
words = content.split()

# Step 3: Insert \r\n every 100000 words
chunks = []
for i in range(0, len(words), CHUNK_SIZE):
    chunk = words[i:i + CHUNK_SIZE]
    chunks.append(' '.join(chunk))

# Step 4: Join chunks with \r\n
result = '\r\n'.join(chunks)

# Step 5: Write the result to the output file
with open(output_file, 'w') as f:
    f.write(result)
