from nltk.chat.util import Chat
def main(inputData):
	pairs=[
		["my name is sukalp",["hi master!"]],
		["how are you",["I am fine"]]
	]
	chat=Chat(pairs)
	outputData = chat.respond(inputData)
	return ""+str(outputData)