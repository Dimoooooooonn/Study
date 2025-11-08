#ifndef ARRAY_H
#define ARRAY_H

#include <iostream>

template <typename T, typename Alloc>
class Array;

template <typename T, typename Alloc>
class Iterator
{
    Array<T, Alloc>* arr;
    int ptr;

public:
    Iterator(Array<T, Alloc>* arr) : arr(arr), ptr(0)
    { }

    Iterator(Array<T, Alloc>* arr, int ptr) : arr(arr), ptr(ptr)
    { }

    T& operator*()
    {
        return arr[ptr];
    }

    void operator++()
    {
        ptr = (ptr < arr->size - 1 ? ptr + 1 : ptr);
    }

    void operator++(int)
    {
        ptr = (ptr < arr->size - 1 ? ptr + 1 : ptr);
    }
};

template <typename T, typename Alloc = std::allocator<T>>
class Array : public Alloc
{
    friend class Iterator<T, Alloc>;

    T* arr;
    size_t size;

private:
    Array(size_t size);

    void swap(Array other);

public:
    Array();
    Array(const T& elem, size_t size);
    Array(const std::initializer_list<T>& list);
    Array(const Array& other);
    ~Array();

    void AddElem(const T& newElem);
    void DeleteElem(int index, int size);
    void DeleteAll();
    Iterator<T, Alloc> begin();
    Iterator<T, Alloc> end();
    size_t GetSize() const noexcept;

    Array& operator=(Array other) noexcept(Array<T, Alloc>::swap);
    T& operator[](size_t index) noexcept;
    const T& operator[](size_t index) const noexcept;

};

template<typename T, typename Alloc>
Array<T, Alloc>::Array(size_t size) : size(size)
{ }

template<typename T, typename Alloc>
void Array<T, Alloc>::swap(Array other)
{
    std::swap(size, other.size);
    std::swap(arr, other.arr);
}

template<typename T, typename Alloc>
Array<T, Alloc>::Array() : size(0)
{ }

template<typename T, typename Alloc>
Array<T, Alloc>::Array(const T& elem, size_t size) : Array(size)
{
    arr = std::allocator_traits<Alloc>::allocate(*this, sizeof(T) * size);

    for (size_t i = 0; i < size; ++i)
        std::allocator_traits<Alloc>::construct(*this, arr + i, elem);
}

template<typename T, typename Alloc>
Array<T, Alloc>::Array(const std::initializer_list<T>& list) : Array(list.size())
{
    arr = std::allocator_traits<Alloc>::allocate(*this, sizeof(T) * size);

    for (size_t i = 0; i < size; ++i)
    {
        std::allocator_traits<Alloc>::construct(*this, arr + i, *(list.begin() + i));
    }
}

template<typename T, typename Alloc>
Array<T, Alloc>::Array(const Array& other) : Array(other.size)
{
    arr = std::allocator_traits<Alloc>::allocate(*this, sizeof(T) * size);

    for (size_t i = 0; i < size; ++i)
    {
        std::allocator_traits<Alloc>::construct(*this, arr + i, other.arr[i]);
    }
}

template<typename T, typename Alloc>
Array<T, Alloc>::~Array()
{
    for (size_t i = 0; i < size; ++i)
        std::allocator_traits<Alloc>::destroy(*this, arr + i);

    std::allocator_traits<Alloc>::deallocate(*this, arr, sizeof(T) * size);
}

template<typename T, typename Alloc>
Iterator<T, Alloc> Array<T, Alloc>::begin()
{
    return Iterator(this, 0);
}

template<typename T, typename Alloc>
Iterator<T, Alloc> Array<T, Alloc>::end()
{
    return Iterator(this, size - 1);
}

template<typename T, typename Alloc>
void Array<T, Alloc>::DeleteAll()
{
    for (size_t i = 0; i < size; ++i)
        std::allocator_traits<Alloc>::destroy(*this, arr + i);

    std::allocator_traits<Alloc>::deallocate(*this, arr, sizeof(T) * size);
    size = 0;
}

template<typename T, typename Alloc>
void Array<T, Alloc>::AddElem(const T& newElem)
{
    T* newArr = std::allocator_traits<Alloc>::allocate(*this, sizeof(T) * (size + 1));
    for (size_t i = 0; i < size; ++i)
    {
        std::allocator_traits<Alloc>::construct(*this, newArr + i, std::move(arr[i]));
    }

    std::allocator_traits<Alloc>::construct(*this, newArr + size, newElem);
    ++size;

    //std::allocator_traits<Alloc>::deallocate(*this, arr, sizeof(T) * size);
    arr = newArr;
}

template<typename T, typename Alloc>
void Array<T, Alloc>::DeleteElem(int index, int size)
{
    if (size > this->size || size <= 0) return;

    T* newArr = std::allocator_traits<Alloc>::allocate(*this, sizeof(T) * (this->size - size));
    int ptr = 0;

    for (size_t i = 0; i < this->size; ++i)
    {
        if (!(i >= index && i < index + size))
        {
            std::allocator_traits<Alloc>::construct(*this, newArr + ptr, std::move(arr[i]));
            ++ptr;
        }
}

    this->size = this->size - size;

    std::allocator_traits<Alloc>::deallocate(*this, arr, sizeof(T) * this->size);
    arr = newArr;
}

template<typename T, typename Alloc>
size_t Array<T, Alloc>::GetSize() const noexcept
{
    return size;
}

template<typename T, typename Alloc>
Array<T, Alloc>& Array<T, Alloc>::operator=(Array other) noexcept(Array<T,Alloc>::swap)
{
    swap(other);
    return *this;
}

template<typename T, typename Alloc>
T& Array<T, Alloc>::operator[](size_t index) noexcept
{
    return arr[index];
}

template<typename T, typename Alloc>
const T& Array<T, Alloc>::operator[](size_t index) const noexcept
{
    return arr[index];
}

#endif // ARRAY_H
