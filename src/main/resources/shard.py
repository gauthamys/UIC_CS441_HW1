def process_file(input_file, output_file, n):
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        buffer = []
        for line_num, line in enumerate(infile.readlines()):
            line = line.replace('\n', '')
            line = line.replace('\r', '')
            word, vector = line.split('\t')
            buffer.append(f"{word}:{vector}")
            # When the buffer reaches n lines, combine and write it to output file
            if (line_num + 1) % n == 0:
                outfile.write('%'.join(buffer) + '\n')
                buffer = []

        # Write any remaining lines in the buffer
        if buffer:
            outfile.write('\t'.join(buffer) + '\n')

# Example usage
input_file = 'embeddings.txt'  # Replace with your input file path
output_file = 'embeddings-sharded.txt'  # Replace with your output file path
process_file(input_file, output_file, 5000)
