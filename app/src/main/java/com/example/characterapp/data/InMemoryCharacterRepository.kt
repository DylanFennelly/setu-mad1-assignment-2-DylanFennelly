package com.example.characterapp.data

class InMemoryCharacterRepository : CharacterRepository {
    private var todoList = mutableListOf<CharacterModel>()

    override fun getTodos(): List<CharacterModel> = todoList.toList()

    override fun addTodo(todo: CharacterModel) {
        todoList.add(todo)
    }

    override fun updateTodo(todo: CharacterModel) {
        val index = todoList.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todoList[index] = todo
        }
    }
}