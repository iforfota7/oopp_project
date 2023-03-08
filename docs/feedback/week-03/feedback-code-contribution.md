# Code Contributions and Code Reviews

### Focused Commits

Grade: **Insufficient**

Feedback: How good your commits were. Were they Focused/Clear?
#### Tops:
- [This commit is focused](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-64/-/commit/0baf7a6e2958362cb46a93f0e7052cdc90e49c54), however it makes the pipeline fail. Please follow this commit but be sure that checkStyle passes for every commit.
#### Tips:
- **No code commits are in main.**
- [Some commit messages are lacking.](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-64/-/commit/80fc5fad5533ea175c36e40106c6f82de5aa42b6) The commit messages should summarise your changes in a concrete fashion. A number is not a good way of showing that.
- [This is also not enough.](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-64/-/commit/6c4bddd78b8e0220e59e65037a984b255220f757)
- [Some commits are not focused.](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-64/-/commit/5fa2dc72484aa2a6beb2fc0dc92a03d49090dc4b) This could have been 3 commits because it hits different parts of your terminology elements.

### Isolation

Grade: **Insufficient**

Feedback: This represents the Isolation of your branches and MRs. Is a branch covering one feature? Is an MR too comprehensive? Did you push code directly to main?
#### Tops:
- The MRs look good in terms of scope, usually there is just one feature that they encapsulate.
#### Tips:
- Only a few MRs are on dev, **none of them are on main**.

### Review-ability
Grade: **Insufficient**

Feedback:
#### Tips:
- The MRs should have a good description. [This](https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-64/-/merge_requests/9) would have been good enough if the code was more well documented.
- The MR above had multiple commits that were not supposed to be there. The branch representing this MR should have been created from dev, or the branch should have been updated to have the latest changes in dev. Please either create branches from dev and make sure to merge dev into the branch before finalising the MR, or drop dev completely.
- Only a few MRs are on dev, **none of them are on main**.
#### Tops:
- Otherwise, if the feedback above was implemented, the MR itself, **given that it has few commits**, would be easy to review.

### Code Reviews
Grade: **Insufficient**

Feedback: **There was no code reviews.**
#### Keep in mind: 
- You should have comments on every MR that are clear and that are addressed and improved upon. 
- The more targeted they are the better. (i.e. if it is evident that they are directed at the lines of code, method or class in question)
- The more improvements I see from the comments, the better.

### Build Server
Grade: **Very good**

Feedback: The pipeline did not fail too often, and when it did, it was on feature branches. Those were also fixed. The build time is fast and there are frequent commits.