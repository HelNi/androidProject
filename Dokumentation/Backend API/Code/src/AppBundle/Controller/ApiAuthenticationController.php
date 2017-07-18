<?php
/**
 * User: nsh
 * Date: 07.07.2017
 * Time: 11:45
 */

namespace AppBundle\Controller;


use AppBundle\Entity\User;
use AppBundle\Service\TokenGeneration\TokenGeneratorInterface;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\FOSRestController;
use FOS\UserBundle\Model\UserManagerInterface;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Security\Core\User\UserChecker;

class ApiAuthenticationController extends FOSRestController
{
    /**
     * Gets the API token for the current user.
     *
     * @return JsonResponse
     * @Rest\Get(name="api_get_token", path="/api/get_token.{_format}")
     * @Rest\QueryParam(name="username", nullable=false)
     * @Rest\QueryParam(name="password", nullable=false)
     * @ApiDoc(output="string",section="Authentication")
     */
    public function getTokenAction(TokenGeneratorInterface $generator, UserManagerInterface $userManager)
    {
        /** @var User $user */
        $user = $this->getUser();
        assert($user);

        // Currently, API keys never expire. So just generate one if it doesn't exist, save it and then return.
        if ($user->getCurrentApiKey() === '') {
            $user->setCurrentApiKey($generator->generateToken($user));
            $userManager->updateUser($user);
        }

        return new JsonResponse($user->getCurrentApiKey());
    }
}