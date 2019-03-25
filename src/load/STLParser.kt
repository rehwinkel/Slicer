package load

class STLParser(inputString: String) {
    val tokens = tokenize(inputString).iterator()
    lateinit var currentToken: Token

    data class Token(val content: String, val start: Int, val end: Int)

    private fun tokenize(text: String): Collection<Token> {
        val tokens = ArrayList<Token>()
        val separators = " \t\n"

        var currentPos = 0
        while (currentPos < text.length) {
            val nextSeparator = text.substring(currentPos).find { it in separators }
            val nextIndex = if (nextSeparator != null) {
                text.substring(currentPos).indexOf(nextSeparator) + currentPos
            } else {
                text.length
            }
            if (currentPos - nextIndex != 0) {
                tokens.add(Token(text.substring(currentPos, nextIndex), currentPos, nextIndex))
                tokens.add(Token(String(charArrayOf(nextSeparator!!)), nextIndex, nextIndex + 1))
            } else if (nextSeparator == '\n' && tokens.last().content != "\n") {
                tokens.add(Token("\n", nextIndex, nextIndex + 1))
            }
            currentPos = nextIndex + 1
        }
        //println(tokens.map { it.content }.joinToString(""))
        return tokens
    }

    fun parse(): ModelData {
        nextSymbol()
        expect("solid")
        expect(" ")
        val headerString = getTokensUntil("\n").joinToString("")

        val vertexData = ModelData(headerString)
        while (!accept("endsolid")) {
            vertexData.add(parseFacet())
        }

        return vertexData
    }

    private fun parseFacet(): ModelData.Face {
        expect("facet")
        expect(" ")
        expect("normal")
        val normal = parseVertex()

        expect("outer")
        expect(" ")
        expect("loop")
        skipTokensUntil("\n")

        expect("vertex")
        val v0 = parseVertex()
        expect("vertex")
        val v1 = parseVertex()
        expect("vertex")
        val v2 = parseVertex()

        expect("endloop")
        skipTokensUntil("\n")

        expect("endfacet")
        skipTokensUntil("\n")
        return ModelData.Face(v0, v1, v2, normal)
    }

    private fun parseVertex(): Vector3 {
        expect(" ")
        val x = getToken().toFloat()
        expect(" ")
        val y = getToken().toFloat()
        expect(" ")
        val z = getToken().toFloat()
        skipTokensUntil("\n")
        return Vector3(x, y, z)
    }

    private fun nextSymbol() {
        if (tokens.hasNext()) {
            currentToken = tokens.next()
        } else {
            throw RuntimeException("EOF")
        }
    }

    private fun accept(accepted: String): Boolean {
        return if (currentToken.content == accepted) {
            nextSymbol()
            true
        } else {
            false
        }
    }

    private fun expect(expected: String): Boolean {
        if (accept(expected)) {
            return true
        } else {
            throw RuntimeException("Unexpected token: '${currentToken.content}'")
        }
    }

    private fun getTokensUntil(terminator: String): Collection<String> {
        val values = ArrayList<String>()
        while (true) {
            val value = getToken()
            if (value == terminator) {
                break
            }
            values.add(value)
        }
        return values
    }

    private fun skipTokensUntil(terminator: String) {
        while (true) {
            if (getToken() == terminator) {
                break
            }
        }
    }

    private fun getToken(): String {
        val content = currentToken.content
        nextSymbol()
        return content
    }

}