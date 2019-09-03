# Contributing

- [Contributing](#contributing)
  - [Contributing Code](#contributing-code)
    - [Build Dependencies](#build-dependencies)
    - [Building the SDK](#building-the-sdk)
    - [Git Commit Guidelines](#git-commit-guidelines)
      - [Commit Message Format](#commit-message-format)
      - [Revert](#revert)
      - [Type](#type)
      - [Scope](#scope)
      - [Subject](#subject)
      - [Body](#body)
      - [Footer](#footer)
    - [Submitting a Pull Request](#submitting-a-pull-request)
  - [Updating the Documentation](#updating-the-documentation)
    - [Set Up Environment (with Bundler)](#set-up-environment-with-bundler)
    - [Compile and Serve Docs](#compile-and-serve-docs)

## Contributing Code

### Build Dependencies

Before you can build the Cisco Webex Java SDK, you will need the following dependencies:

- Java 1.6
- [Apache Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)

### Building the SDK

Fork the [webex-java-sdk](https://github.com/webex/webex-java-sdk) repository and `git clone` your fork:

```bash
git clone https://github.com/your-username/webex-java-sdk.git
```

Building the project from here is dependant on your environment and tooling.

### Git Commit Guidelines

As part of the build process, commits are run through [conventional changelog](https://github.com/conventional-changelog/conventional-changelog)
to generate the changelog. Please adhere to the following guidelines when formatting your commit messages.

#### Commit Message Format

Each commit message consists of a **header**, a **body** and a **footer**. The header has a special format that includes a **type**, a **scope** and a **subject**:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

The **header** is mandatory and the scope of the header is optional.

Any line of the commit message cannot be longer 100 characters! This allows the message to be easier to read on GitHub as well as in various git tools.

#### Revert

If the commit reverts a previous commit, it should begin with `revert:`, followed by the header of the reverted commit. In the body it should say: `This reverts commit <hash>`., where the hash is the SHA of the commit being reverted.

#### Type

Must be one of the following:

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing tests
- **chore**: Changes to the build process or auxiliary tools and libraries such as documentation generation

#### Scope

The scope should indicate what is being changed. Generally, these should match class/package names. For example, `core`, `common`, `http`, etc. Other than package names, `tooling` tends to be the most common.

#### Subject

The subject contains succinct description of the change:

- use the imperative, present tense: "change" not "changed" nor "changes"
- don't capitalize first letter
- no dot (.) at the end

#### Body

Just as in the **subject** the imperative, present tense: "change" not "changed" nor "changes". The body should include the motivation for the change and contrast this with previous behavior.

#### Footer

The footer should contain any information about **Breaking changes** and is also the place to reference GitHub issues that this commit **closes**.

**Breaking Changes** should start with the word `BREAKING CHANGE:` with a space or two newlines. The rest of the commit message is then used for this.

### Submitting a Pull Request

Prior to developing a new feature, be sure to search the [Pull Requests](https://github.com/webex/webex-java-sdk/pulls) for your idea to ensure you're not creating a duplicate change. Then, create a development branch in your forked repository for your idea and start coding!

When you're ready to submit your change, first check that new commits haven't been made in the upstream's `master` branch. If there are new commits, rebase your development branch to ensure a fast-forward merge when your Pull Request is approved:

```bash
# Fetch upstream master and update your local master branch
git fetch upstream
git checkout master
git merge upstream/master

# Rebase your development branch
git checkout feature
git rebase master
```

Finally, open a [new Pull Request](https://github.com/webex/webex-java-sdk/compare) with your changes. Once your request is opened, a developer will review, comment, and, when approved, merge your changes!