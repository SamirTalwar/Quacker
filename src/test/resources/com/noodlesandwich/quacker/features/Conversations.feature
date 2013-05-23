Feature: Conversations
  Background:
    Given there is a user named Abhishek
    And there is a user named Bobby
    And there is a user named Sanjay
    And Abhishek follows Bobby
    And Abhishek follows Sanjay
    And Bobby follows Abhishek
    And Bobby follows Sanjay
    And Sanjay follows Abhishek
    And Sanjay follows Bobby

  Scenario: Bobby wants to view the conversation around one of Abhishek's tweets.
    Given the timeline:
      | Bobby    | I don't like yellow.                                |
      | Abhishek | @Bobby What's your favourite colour?                |
      | Bobby    | @Abhishek Blue.                                     |
      | Sanjay   | @Abhishek Green.                                    |
      | Bobby    | @Abhishek No, wait, purple.                         |
      | Abhishek | I think Bobby has problems with decision-making.    |
      | Abhishek | @Bobby Make up your mind.                           |
      | Sanjay   | @Bobby You told me your favourite colour was black! |
      | Abhishek | (aside) My friends are the dumbest.                 |
      | Bobby    | @Sanjay It is!                                      |

    When Sanjay opens up his feed
    And clicks on the quack from Abhishek that says "@Bobby What's your favourite colour?"
    Then he should see:
      | Bobby    | I don't like yellow.                                |
      | Abhishek | @Bobby What's your favourite colour?                |
      | Bobby    | @Abhishek Blue.                                     |
      | Sanjay   | @Abhishek Green.                                    |
      | Bobby    | @Abhishek No, wait, purple.                         |
      | Abhishek | @Bobby Make up your mind.                           |
      | Sanjay   | @Bobby You told me your favourite colour was black! |
      | Bobby    | @Sanjay It is!                                      |
