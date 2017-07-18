<?php
/**
 * User: nsh
 * Date: 10.07.2017
 * Time: 11:59
 */

namespace AppBundle\Controller;


use AppBundle\Entity\Entry;
use AppBundle\Entity\User;
use AppBundle\Form\EntryType;
use AppBundle\Form\TimeSpanType;
use Doctrine\ORM\EntityManager;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\FOSRestController;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Security;
use Symfony\Component\HttpFoundation\Request;

/**
 * Class CategoryApiControllerController
 * @package AppBundle\Controller
 *
 * @Rest\RouteResource("Entry")
 */
class EntryApiController extends FOSRestController
{
    /**
     * @ApiDoc(input={"class": "AppBundle\Form\TimeSpanType", "name": ""}, section="Entry", authentication=true, resource=true)
     */
    public function cgetBetweenAction(Request $request)
    {
        $user = $this->getUser();

        return $this->betweenEntriesFor($request, $user);
    }

    /**
     * Gets an Entry by ID.
     * Will not Allow non.admins to retrieve entities from other users.
     *
     * @param int $id
     * @param EntityManager $entityManager
     * @return \AppBundle\Entity\Entry|null|object
     * @ApiDoc(output="AppBundle\Entity\Entry", section="Entry", authentication=true, resource=true)
     */
    public function getAction($id, EntityManager $entityManager)
    {
        if ($this->isGranted('ROLE_ADMIN')) {
            return $entityManager->find('AppBundle:Entry', $id);
        }

        $user = $this->getUser();

        return $entityManager->getRepository('AppBundle:Entry')
            ->findOneBy([
                'user' => $user,
                'id' => $id
            ]);
    }

    /**
     * Gets all Entries for the current user.
     *
     * @param array $ids
     * @param EntityManager $em
     * @return \AppBundle\Entity\Entry[]|array
     * @ApiDoc(output="AppBundle\Entity\Entry", section="Entry",authentication=true, resource=true)
     */
    public function cgetAction()
    {
        return $this->getDoctrine()->getRepository('AppBundle:Entry')
            ->findBy(['user' => $this->getUser()], ['start' => 'ASC']);
    }

    /**
     * Gets all entries for the given user between the two dates
     *
     * @param string $userName
     * @Security("is_granted('ROLE_ADMIN')")
     * @Rest\Get("/entries/between_for/{userName}")
     * @ApiDoc(section="Entry", authentication=true, authenticationRoles={"ROLE_ADMIN"}, input={"class":"AppBundle\Form\TimeSpanType", "name":""}, resource=true)
     */
    public function cgetBetweenFor($userName, Request $request)
    {
        $user = $this->get('fos_user.user_manager')
            ->findUserByUsernameOrEmail($userName);

        if (!$user) {
            throw $this->createNotFoundException("User $userName not found");
        }

        return $this->betweenEntriesFor($request, $user);
    }

    /**
     * Create a new Entry.
     * @ApiDoc(input="", section="Entry", input={"class": "AppBundle\Form\EntryType", "name": ""}, authentication=true, resource=true)
     * @Rest\View(statusCode=201)
     */
    public function postAction(Request $request)
    {
        $entry = new Entry();
        $entry->setUser($this->getUser());

        $form = $this->get('form.factory')
            ->createNamed('', EntryType::class, $entry, ['csrf_protection' => false]);

        $form->handleRequest($request);
        if ($form->isValid()) {
            $em = $this->get('doctrine.orm.default_entity_manager');
            $em->persist($entry);
            $em->flush();
        }

        return $form;
    }

    /**
     * @param Entry $entry
     * @param Request $request
     * @ApiDoc(section="Entry", input={"class":"AppBundle\Form\EntryType", "name": ""}, authentication=true, resource=true)
     * @Security("is_granted('EDIT', entry)")
     * @return \Symfony\Component\Form\FormInterface
     */
    public function putAction(Entry $entry, Request $request)
    {
        $form = $this->get('form.factory')
            ->createNamed('', EntryType::class, $entry, ['csrf_protection' => \false, 'method' => 'put']);

        $form->handleRequest($request);
        $this->get('doctrine.orm.default_entity_manager')->flush();

        return $form;
    }

    /**
     * @param Entry $entry
     * @Security("is_granted('DELETE', entry)")
     * @ApiDoc(section="Entry", authentication=true, resource=true)
     * @Rest\View(statusCode=204)
     */
    public function deleteAction(Entry $entry)
    {
        $em = $this->get('doctrine.orm.default_entity_manager');
        $em->remove($entry);
        $em->flush();
    }

    /**
     * @param Request $request
     * @param $user
     * @return array|\Symfony\Component\Form\FormInterface
     */
    private function betweenEntriesFor(Request $request, $user)
    {
        $form = $this->get('form.factory')
            ->createNamed('', TimeSpanType::class, null, ['method' => 'get', 'csrf_protection' => \false]);

        $form->submit($request->query->all());
        if (!$form->isValid()) {
            return $form;
        }

        $qb = $this->get('doctrine.orm.default_entity_manager')
            ->getRepository('AppBundle:Entry')
            ->createQueryBuilder('entry');

        return $qb
            ->where(
                $qb->expr()->orX(
                    $qb->expr()->between('entry.start', ':begin', ':end'),
                    $qb->expr()->between('entry.end', ':begin', ':end')
                )
            )
            ->setParameter('begin', $form->getData()['start'])
            ->setParameter('end', $form->getData()['end'])
            ->andWhere('entry.user = :user')
            ->setParameter('user', $user)
            ->orderBy('entry.start', 'ASC')
            ->getQuery()->getResult();
    }
}