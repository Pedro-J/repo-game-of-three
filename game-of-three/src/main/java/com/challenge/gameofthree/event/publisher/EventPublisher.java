package com.challenge.gameofthree.event.publisher;


import com.challenge.gameofthree.exception.EventPublishExpcetion;

interface EventPublisher<T> {

    void publish(T event) throws EventPublishExpcetion;

}
