Feature: the Timeline
  Background:
    Given there is a user named Abhishek
    And there is a user named Bobby

  Scenario: Abhishek posts to his timeline, and others can read it.
    Given Abhishek quacks "Hey, everybody! I got Quacker!"
    When Bobby opens up Abhishek's timeline
    Then he should see:
      | Hey, everybody! I got Quacker! |