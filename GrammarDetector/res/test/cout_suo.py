#coding:utf-8
import sys
import os

def count_suo(in_path, out_path):
	word_freq_dict = {}
	for line in open(in_path):
		tokens = line.strip().split(' ')
		for t in tokens:
			if "'" in t:
				add_2_dict(t, word_freq_dict)
	sorted_items = sorted(word_freq_dict.items(), key = lambda asd:asd[1], reverse=True)

	out_file = open(out_path, 'wb')
	for k, v in word_freq_dict.items():
		out_file.write(k + '\t' + str(v) + '\n')
	out_file.close()

def add_2_dict(token, word_freq_dict):
	if word_freq_dict.has_key(token):
		word_freq_dict[token] = word_freq_dict[token] + 1
	else:
		word_freq_dict[token] = 1

def avg_len(in_path):
	total = 0
	for i, line in enumerate(open(in_path)):
		tokens = line.strip().split()
		total += len(tokens)
	print total / ( i + 1)

if __name__ == '__main__':
	#count_suo('sents.txt', 'suo.tsv')
	avg_len('sents.txt')

