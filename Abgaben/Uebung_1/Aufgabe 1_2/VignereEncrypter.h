
#pragma once

#include <string>
#include <vector>

class VigenereEncrypter {
private:
	std::string &key;
	int keyLength;

	void helper(std::string* in, std::string* out, bool encrypt);
public:
	static const int ALPHABET_SIZE = 27;
	static const int ALPHABET_OFFSET = 97;
	static const int SPACE_VALUE = 123;

	VigenereEncrypter(std::string *key);
	void encrypt(std::string* in, std::string* encrypted);
	void decrypt(std::string* in, std::string* decrypted);
};
