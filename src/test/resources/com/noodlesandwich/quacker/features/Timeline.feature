Feature: the Timeline
  Background:
    Given there is a user named Abhishek
    And there is a user named Bobby
    And there is a user named Sanjay
    And Sanjay follows Abhishek
    And Sanjay follows Bobby

  Scenario: Abhishek posts to his timeline, and others can read it.
    Given Abhishek quacks "Hey, everybody! I got Quacker!"
    When Bobby opens up Abhishek's timeline
    Then he should see:
      | Abhishek | Hey, everybody! I got Quacker! |

  Scenario: Sanjay follows his friends, and can read their posts on his feed.
    Given a bunch of quacks from Abhishek
    And a bunch of quacks from Bobby
    And a bunch of quacks from Sanjay

    Given Abhishek quacks "Knock knock."
    And Bobby quacks "@Abhishek Who's there?"
    And Abhishek quacks "@Bobby Doris."
    And Bobby quacks "@Abhishek Doris who?"
    And Abhishek quacks "@Bobby Doris locked. That's why I'm knocking!"
    And Sanjay quacks "@Abhishek @Bobby You guys suck."
    When Sanjay opens up his feed
    Then he should see:
      | Sanjay   | @Abhishek @Bobby You guys suck.               |
      | Abhishek | @Bobby Doris locked. That's why I'm knocking! |
      | Bobby    | @Abhishek Doris who?                          |
      | Abhishek | @Bobby Doris.                                 |
      | Bobby    | @Abhishek Who's there?                        |
      | Abhishek | Knock knock.                                  |
      | ...                                                      |
