Feature: the Timeline
  Background:
    Given there is a user named Abhishek
    And there is a user named Bobby
    And there is a user named Sanjay
    # And Sanjay follows Abhishek
    # And Sanjay follows Bobby

  Scenario: Abhishek posts to his timeline, and others can read it.
    Given Abhishek quacks "Hey, everybody! I got Quacker!"
    When Bobby opens up Abhishek's timeline
    Then he should see:
      | Hey, everybody! I got Quacker! |

  @wip
  Scenario: Sanjay follows his friends, and can read their posts on his feed.
    Given Abhishek quacks "Knock knock."
    And Bobby quacks "@Abhishek Who's there?"
    And Abhishek quacks "@Bobby Doris."
    And Bobby quacks "@Abhishek Doris who?"
    And Abishek quacks "@Bobby Doris locked. That's why I'm knocking!"
    And Sanjay quacks "@Abhishek @Bobby You guys suck."
    When Sanjay opens up his feed
    Then he should see:
      | @Abhishek @Bobby You guys suck.               |
      | @Bobby Doris locked. That's why I'm knocking! |
      | @Abhishek Doris who?                          |
      | @Bobby Doris.                                 |
      | @Abhishek Who's there?                        |
      | Knock knock.                                  |
