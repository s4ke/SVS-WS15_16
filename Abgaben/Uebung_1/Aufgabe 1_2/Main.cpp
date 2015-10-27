//Martin Braun (1249080), Julian Neuberger (1252734)
#include <iostream>
#include "VignereEncrypter.h"
using namespace std;

int main(){
	std::string in;
	std::string key;
	char whatToDo;
	bool decided = false;
	bool desicion;
		
	cout << "Enter the message to de/encrypt" << std::endl;
	cin >> in;

	cout << "Enter a key" << std::endl;
	cin >> key;

	VigenereEncrypter encrypter(&key);
	std::string out;

	cout << "Do you want to encrypt (e) or decrypt (d)?" << std::endl;
	while (!decided) {
		cin >> whatToDo;
		if(whatToDo == 'e' || whatToDo == 'E') {
			encrypter.encrypt(&in, &out);
			decided = true;
		} else if( whatToDo == 'd' || whatToDo == 'D') {
			encrypter.decrypt(&in, &out);
			decided = true;
		} else {
			cout << "Please enter either e or d!";
		}
	}
	
	cout << "Result: " << out << std::endl;

	return 0;
}