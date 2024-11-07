# An Orderbook Implementation

This document provides the information and instructions required to implement a orderbook using the template Java classes provided.

## Table of Content

1. [What is an Orderbook](#what-is-an-orderbook)
2. [The Task](#the-task)

## What is an Orderbook

An order book is a ledger of buy and sell orders for a financial instrument on an exchange. It shows the current bids and asks, helping traders understand
market depth and liquidity. It's used to analyze market sentiment and make informed trading decisions. Orders are matched, trades are executed, and the order
book is continuously updated in real-time. In the context of Market Surveillance, the goal is to monitor orderbook activity and alert when suspicious and
malicious trading activity is occurring.

**Order Book Example**

| Level | Bids (Buy Orders) | Quantity | Asks (Sell Orders) | Quantity |
|-------|-------------------|----------|--------------------|----------|
| 1     | $10.50            | 100      | $11.00             | 50       |
| 2     | $10.40            | 200      | $11.10             | 75       |
| 3     | $10.30            | 150      | $11.20             | 100      |
| 4     | $10.20            | 300      | $11.30             | 120      |
| 5     | $10.10            | 100      | $11.40             | 200      |

Price Level 1 represents the current best bid/ask offer, and as we move up the price levels, the offer decrease. The discrepancy between the
prices at Level 1 is referred to as the spread. When an order enters the order book and overlap the spread, a trade is executed. If multiple orders exist at a
particular price level, the first order to enter the book is given priority through the first in, first out (FIFO) principle.

## The Task

The task is to implement **all** methods of the
[FifoOrderbook.java](src/main/java/com/scila/codetest/fifoorderbook/FifoOrderbook.java) such that the test cases
in [FifoOrderbookTest.java](src/test/java/com/scila/codetest/fifoorderbook/FifoOrderbookTest.java), and potentially other test cases, pass. The tests simple
add,
update and remove orders in theorderbook then check the state of the order book. No matching is expected.

__NOTE__: Do not alter any of the **other** already existing classes.

The goal is to write **clean**, **fast** and **memory efficient** code that in theory can handle **tens of thousands** of orderbook updates and orderbook
queries **per second** while adhering to the business perspective.
There are no restrictions in regard to adding more classes etc, but no third part dependencies are allowed.


